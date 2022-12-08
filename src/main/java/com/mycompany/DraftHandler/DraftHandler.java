/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DraftHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.sumoproject.SumoProject;

import java.util.HashSet;

/*
TODO add in exceptions, want GUI to be able to react and respond to specific situations instead of just blanket TRUE FALSE return values

*/

/**
 * Handler for Fantasy Sumo Draft. Handles all logic and tracking of the draft, such as
 * who drafts who, team sizes and types, and
 * @author blarg
 */
public class DraftHandler {

    // METHODS
    // =========================================================================
    /**
     * Default Constructor
     *
     * Creates a draft with a single player, "Drafter 1"
     */
    public DraftHandler() {
        this("Drafter 1");
    }

    /**
     * Initializes a draft with a provided array of players.
     * @param players_ Array of player names
     */
    public DraftHandler(String... players_) {
        this(Arrays.asList(players_));
    }

    public DraftHandler(List<String> players_) {
        this(players_, "", new int[]{1}, true, "linear");
    }

    /**
     * DraftHandler
     * @param players_
     * @param basho_
     * @param divisionSizes_
     * @param isRand
     * @param draftType
     */
    public DraftHandler(List<String> players_, String basho_,
            int[] divisionSizes_, boolean isRand, String draftType) {

        SQL_TBL_NAME = "drafteeTest2";
        this.isDraftOngoing = true;
        this.divisionSizes = new HashMap<>();
        this.teamSize = 0;
        this.basho = basho_;

        for (int i = 0; i < divisionSizes_.length; i++) {
            this.teamSize += divisionSizes_[i];
            this.divisionSizes.put(i+1, divisionSizes_[i]);
        }

        if (isRand) Collections.shuffle(players_);

        players = List.copyOf(players_);
        activeDrafter = players.get(0);

        this.draftOrder = new ArrayList<>();
        if (draftType.toLowerCase().equals("snake")) {
            for (int i = 0; i < teamSize; i++) {
                this.draftOrder.addAll(players_);
                Collections.reverse(players_);
            }
        }
        else {
            // Default is a Linear Draft
            for (int i = 0; i < teamSize; i++) this.draftOrder.addAll(players);
        }

        playerDraftTbl = new LinkedHashMap<>();
        for (var p : players_) playerDraftTbl.put(p, new Roster(teamSize));

        draftPos = 1;
        draftLog = new ArrayList<>(){ {
                add( "Draft Begun: Starting with %s".formatted(activeDrafter) );
            } };

        draftedRikishi = new HashSet<>();
    }

    /**
     * @return Draft Order in the form of a list of player names
     */
    public List<String> getDraftOrder() {
        return this.draftOrder;
    }

    /**
     * @return Name of player actively drafting
     */
    public String getActiveDrafter() {
        return activeDrafter;
    }

    /**
     * @return List of players in no particular order
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * @return Total Team Size for each individual
     */
    public int getTeamSize() {
        return teamSize;
    }

    /**
     * @return current draft pos 0-indexed
    */
    public int getDraftPos() {
        return draftPos - 1;
    }

    public String getBasho() {
        return this.basho;
    }

    /**
     * Get a Draft event. If a {@code idx_args} argument is provided, then that logged
     * event is returned. Otherwise, method defaults to returning the most recent (ie.
     * last) even to occur.
     * @param idx_args Draft event index number to pull {@code default = log_size - 1}
     * @return
     */
    public String getDraftEvent(int... idx_args) throws IndexOutOfBoundsException {
        int idx = idx_args.length > 0 ? idx_args[0] : draftLog.size() - 1;
        return draftLog.get(idx);
    }

    /**
     * Get the roster for the provided {@code player} in a list of list of strings
     * @param player
     * @return
     */
    public List< List<String> > getPlayerRoster(String player) {
        return playerDraftTbl.get(player).asList();
    }

    /**
     * Add {@code rikishi} to the {@code activeDrafters} Roster Table as well as
     * all the related cleanup actions. Adds the the draft event to
     * {@code draftLog}, adds the drafted rikishi to the set of drafted rikishi,
     * and increments the draft counter/
     *
     * When drafting a rikishi, checks that the current roster does not violate
     * the division settings.
     *
     * @param rikishi Name of the rikishi to add/draft
     * @param rank rank of the rikishi
     * @param div division of the rikishi
     * @return
     * -1 : Selected Rikishi ahs been
     */
    public int draftRikishi(String rikishi, String rank, int div) {
        return draftRikishiToTeam(activeDrafter, rikishi,rank,div);
    }

    /**
     * Add a {@code rikishi} to a given team. Updates the SQL draft table, updates stored tables,
     * updates {@code draftLog} with the event, and marks the drafted {@code rikishi} as
     * drafted and unavailable.
     *
     * @param drafter Team to add {@code rikishi} to
     * @param rikishi Rikishi to add to {@code team}
     * @param rank Rikishi rank
     * @param div Rikishi Division
     * @return
     * 0    : successful draft, draft continues
     * 1    : successful darft, draft is finished
     * -1   : Selected Rikishi has already been selected
     * -2   : Selected Rikishi breaks division parameters
     */
    public int draftRikishiToTeam(String drafter, String rikishi, String rank, int div) {
        // TODO need to figure out how to include some validation checking

        if (draftedRikishi.contains(rikishi)) {
            return -1;
        }

        Roster currRoster = playerDraftTbl.get(drafter);
        if ( currRoster.getDivisionCount(div) < this.divisionSizes.get(div) )
            currRoster.addRikishi( draftPos, rikishi, rank, div );
        else {
            return -2;
        }

        draftLog.add(
            "%d :: %s added to team %s\n".formatted(draftPos, rikishi, drafter)
        );

        incDraftCnt();
        draftedRikishi.add(rikishi);
        if ( isDraftFinished() )
            return 1;
        else
            return 0;
    }

    /**
     * Check if the rikishi with the shikona {@code name} has been drafted
     * @param name Name of shikona
     * @return {@code true} if {@code name} has been drafted, otw {@code false}
     */
    public boolean isRikishiDrafted(String name) {
        return draftedRikishi.contains(name);
    }

    public Map<Integer, Integer> getDivisionSizes() {
        return divisionSizes;
    }

    /**
     * Finishes up the draft by updating the drafteeSql Table
     */
    public void finishDraft() {
        String mainUpdtStr =
            "UPDATE %s ".formatted(SQL_TBL_NAME)
            + " SET drafter = '%s' "
            + " WHERE shikona = '%s' ";
        for (var player : playerDraftTbl.keySet()) {
            var roster = playerDraftTbl.get(player).asList();
            for (var rikishi : roster) {
                String updtStr = String.format(mainUpdtStr, player, rikishi.get(1));
                SumoProject.sqlHandler.executeUpdate(updtStr);
            }
        }
        return;
    }

    /**
     * Draft Count wrapper, updates the active drafter and increments draft
     * counter
     */
    private void incDraftCnt() {
        activeDrafter = this.draftOrder.get(draftPos);
        draftPos++;
    }

    /**
     * Checker for if the draft is finished. Checks if the draft position
     * has exceeded the draft order
     * @return {@code true} if {@code draftPos > draftOrder.size()}
     * else {@code false}
     */
    private boolean isDraftFinished() {
//        return (draftPos >= draftOrder.size());
        return true;
    }

    // MEMBERS
    // =========================================================================
    /**
     * Name of the currently active drafter
     */
    private String activeDrafter;

    /**
     * Current position in the draft
     */
    private Integer draftPos;

    /**
     * Draft Log detailing the events and when they occur
     */
    private List<String> draftLog;

    /**
     * Set of drafted rikishi, presence in set indicates rikishi has been drafted
     */
    private HashSet<String> draftedRikishi;

    /**
     * A Hash Table that maps a Roster to a players name
     */
    private Map < String, Roster > playerDraftTbl;

    /**
     * List of the names of players
     */
    private List<String> players;

    /**
     * Draft order by name of player
     */
    private List<String> draftOrder;

    /**
     * Total team size across all divisions
     */
    private Integer teamSize;

    /**
     * A map of the number of rikishi each team is allowed to have for each division
     */
    private Map<Integer, Integer> divisionSizes;

    /**
     * The basho in string form that the draft handler is handling
     */
    private String basho;

    /**
     * Name of the sql table that this draft is updating
     */
    private final String SQL_TBL_NAME;

    private boolean isDraftOngoing;
}
