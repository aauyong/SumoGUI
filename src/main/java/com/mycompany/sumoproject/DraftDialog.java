/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sumoproject;

import com.mycompany.DraftHandler.DraftHandler;
import com.mycompany.SQLHandler.SQLTable;
import com.mycompany.Tables.BanzDraftTbl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * A Jdialog box for conducting a draft.
 *
 *
 *
 * @author blarg
 */
public class DraftDialog extends JDialog {

    /**
     * Constructs a draft dialog box with the provided settings and a draft handler. The
     * dialog box is constructed of three components:
     *
     * A tracking panel which contains information pertaining to the draft order, event log, and current draft
     *
     * A banzuke Panel which contains the banzuke tables for each division that is chosen
     * to be drafted from
     *
     * And a players panel, which holds tables for each player and their currently
     * drafted roster
     *
     * @see com.mycompany.Tables.BanzDraftTbl
     * @see com.mycompany.DraftHandler
     *
     * @param players
     * @param divSizes
     * @param isRand
     * @param draftType
     */
    public DraftDialog(java.awt.Window owner, JDialog.ModalityType modal,
            List<String> players, int[] divSizes, boolean isRand, String draftType) {
        super(owner,modal);
        drftHndlr = new DraftHandler(
                players,
                Helpers.getMostRecentBasho(),
                divSizes,
                isRand,
                draftType
        );

        List<String> playerOrdr = drftHndlr.getPlayers();
        Integer teamSize = drftHndlr.getTeamSize();
        Set<Integer> divs = drftHndlr.getDivisionSizes().keySet();
        String basho = drftHndlr.getBasho();

        initTrackingMembers();
        initTrackingPnlLayout();
        initBanzPnlMembers(basho, divs);
        initBanzPnlLayout();
        initPlyrsPnlMmbrs(playerOrdr, teamSize);
        initPlyrsPnlLayout(playerOrdr);
        initWindow();
        initWindowLayout();

        currSlctdCache = new HashMap<>();
        currSlctdCache.put("shikona", null);
        currSlctdCache.put("rank", null);
        currSlctdCache.put("division", null);
    }

    /**
     * Initialize the dialog window itself to dispose on close and assign the
     * {@code windowClosing} action.
     */
    private void initWindow() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                actionDialogWindowClosing(evt);
            }
        });

        setResizable(false);
    }

    /**
     * Initialize the dialog window layout itself, inputting the three panels
     * into place
     *
     * LAYOUT
     *   0 1
     * 0 A B
     * 1 C c
     */
    private void initWindowLayout() {
        setLayout(new java.awt.GridBagLayout());

        // A
        add(banzPnl, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 0;
            }
        });
        // B
        add(trackingPnl, new java.awt.GridBagConstraints() {
            {
                gridx = 1;
            }
        });

        // C
        add(plyrsPnl, new java.awt.GridBagConstraints() {
            {
                gridx = 0;
                gridy = 1;
                gridwidth = 2;
            }
        });
    }

    /**
     * Initialize the Data Members of the tracking panel, allocating
     * memory, setting the sizes, and any other parameters like text, tooltip
     * text, and any other parameters
     *
     * draftOrdrLst : Add the draft order to the list and set the selected value
     * to be the first index
     *
     * draftOrdrScrll : Set viewport to be {@code draftOrdrLst} and
     */
    private void initTrackingMembers() {
        trackingPnl = new javax.swing.JPanel();

        draftOrdrLst = new javax.swing.JList<String>(){};
        String[] drftOrdrArr = drftHndlr.getDraftOrder().toArray(String[]::new);
        for (int i = 0; i < drftOrdrArr.length; i++) {
            drftOrdrArr[i] = "%d: ".formatted(i+1) + drftOrdrArr[i];
        }
        draftOrdrLst.setListData( drftOrdrArr );
        draftOrdrLst.setSelectedIndex(0);

        draftOrdrLst.setToolTipText("Draft Ordering");
        draftOrdrLst.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                draftOrdrLst.setSelectedIndex(drftHndlr.getDraftPos());
            }
        });

        draftOrdrScrll = new javax.swing.JScrollPane();
        draftOrdrScrll.setEnabled(false);
        draftOrdrScrll.setPreferredSize(new java.awt.Dimension(150, 200));
        draftOrdrScrll.setViewportView(draftOrdrLst);

        draftEvtLog = new javax.swing.JTextArea();
        draftEvtLog.setEditable(false);
        draftEvtLog.setColumns(20);
        draftEvtLog.setRows(5);

        draftEvtLogScrll = new javax.swing.JScrollPane();
        draftEvtLogScrll.setPreferredSize(new java.awt.Dimension(250, 200));
        draftEvtLogScrll.setViewportView(draftEvtLog);

        draftRstBut = new javax.swing.JButton();
        draftRstBut.setText("Reset");
        draftRstBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionResetActionPerformed(evt);
            }
        });

        draftSlctBut = new javax.swing.JButton();
        draftSlctBut.setText("Draft");
        draftSlctBut.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    actionSelectButActionPerformed(evt);
                } catch (Exception ex) {
                    Logger.getLogger(DraftDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        currDrafterLbl = new javax.swing.JLabel();
    }

    /**
     * Initialize the layout of the tracking pane, which tracks the event logs
     * draft order, and current drafter.
     *
     *
     * In the layout below,each element is denoted with a uppercase letter
     * (A, B, C...) that indicates it's origin point. The lowercase alternative
     * indicates the size of the component.
     *
     *    0 1
     * 0: A B
     * 1: a b
     * 2: a b
     * 3: a b
     * 4: a b
     * 5: a b
     * 6: a b
     * 7: C D
     * 8: E
     */
    private void initTrackingPnlLayout() {
        trackingPnl.setLayout(new java.awt.GridBagLayout());
        // A
        trackingPnl.add(draftOrdrScrll, new java.awt.GridBagConstraints() {
            {
                gridx = 4;
                gridy = 0;
                gridheight = 7;
            }
        });

        // B
        trackingPnl.add(draftEvtLogScrll, new java.awt.GridBagConstraints() {
            {
                gridx = 5;
                gridy = 0;
                gridheight = 7;
            }
        });

        // e
        trackingPnl.add(currDrafterLbl, new java.awt.GridBagConstraints() {
            {
                gridx = 4;
                gridy = 7;
            }
        });

        // f
        trackingPnl.add(draftSlctBut, new java.awt.GridBagConstraints() {
            {
                gridx = 4;
                gridy = 8;
            }
        });

        // g
        trackingPnl.add(draftRstBut, new java.awt.GridBagConstraints() {
            {
                gridx = 5;
                gridy = 8;
            }
        });
    }

    /**
     * Initialize the banzuke tables by inserting the appropriate banzuke into
     * them. Banzukes(s) for the {@code basho} and division selected from
     * {@code divs} are inserted into each table to be displayed.
     *
     * @param basho Which basho to get the banzuke from
     * @param divs Divisions to create banzukes for
     */
    private void initBanzPnlMembers(String basho, Set<Integer> divs) {
        banzPnl = new javax.swing.JPanel();
        banzScrlls = new ArrayList<>();
        String banzukeQry = """
                EXECUTE dbo.upc_getBanzuke '%s', %s;
                """.formatted(basho, "%d");
        for (var div : divs) {
            BanzDraftTbl banzTbl = new BanzDraftTbl(drftHndlr.getBasho(), div);

            banzTbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            banzTbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    actionBanzTblMouseReleased(banzTbl, evt);
                }
            });

            javax.swing.JScrollPane banzScrll = new javax.swing.JScrollPane(){};

            SQLTable res = SumoProject.sqlHandler.executeQuery(banzukeQry.formatted(div));
            banzTbl.insertBanzuke2(res.getData());
            banzScrll.setViewportView(banzTbl);

            var dim = banzTbl.getPreferredSize();
            dim.setSize(dim.width, banzTbl.getRowCount() * banzTbl.getRowHeight() + 28);
            banzScrll.setPreferredSize(dim);

            banzScrlls.add(banzScrll);
        }
    }

    /**
     * Initiate and insert banzuke tables into the {@code xtraDivsBanzTbl} panel
     * for any extra specified divisions.
     *
     * Each table is inserted into the xtra panel and gronw
     * @param divs Set of divisions to draw banzukes for
     */
    private void initBanzPnlLayout() {
        banzPnl.setLayout(new java.awt.GridBagLayout());

        int x = 0;
        for (var scrll : banzScrlls) {
            java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
            gbc.gridx = x++;
            gbc.anchor = java.awt.GridBagConstraints.PAGE_START;
            banzPnl.add(scrll, gbc);
        }
    }

    /**
     * Initialize the Players Panel, which contains all the players roster
     * tables. Creates Roster Tables of an appropriate size, {@code teamSize}.
     *
     * @param numOfPlayers Indicates how many tables are initialized
     * @param teamSize Indicates the size of the tables to be created
     */
    private void initPlyrsPnlMmbrs(List<String> players, Integer teamSize) {
        plyrsPnl = new javax.swing.JPanel();
        plyrScrlls = new HashMap<>();
        plyrLbls = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {

            JTable plyrTbl = new javax.swing.JTable(
                new Object[teamSize][4],
                List.of(
                        "Draft #",
                        "Shikona",
                        "Rank",
                        "Division").toArray(Object[]::new)
            ){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };


            plyrScrlls.put(
                players.get(i)
                ,new javax.swing.JScrollPane() {
                {
                    setViewportView(plyrTbl);
                    setPreferredSize(new java.awt.Dimension(250, 22*teamSize));
                }
            });

            plyrLbls.add(new javax.swing.JLabel( players.get(i) ));
        }
    }

    /**
     * Initialize the Players Panel Layout, inserting and laying out JTables
     * from {@code plyrsScrlls} and labels from {@code plyrLbls}. Both are initiated in
     * the {@code initPlyrsPnlMmbrs} method.
     *
     * Each scrollpane has its corresponding label inserted above it to denote the owner
     * of the roster table. The layout is defined by the {@code MAX_ROW_SIZE} local var.
     * If the number of tables in a row exceeds this amount, then a new row is started.
     */
    private void initPlyrsPnlLayout(List<String> players) {
        plyrsPnl.setLayout(new java.awt.GridBagLayout());
        final int MAX_ROW_SIZE = 4;
        int x = 0, y = 0;
        java.awt.GridBagConstraints gbc;
        for (int i = 0; i < players.size(); i++) {
            gbc = new java.awt.GridBagConstraints();
            gbc.gridx = (x) % MAX_ROW_SIZE;
            gbc.gridy = y;
            plyrsPnl.add(plyrLbls.get(i), gbc);

            gbc = new java.awt.GridBagConstraints();
            gbc.gridx = (x) % MAX_ROW_SIZE;
            gbc.gridy = y + 1;
            plyrsPnl.add(plyrScrlls.get(players.get(i)), gbc);

            x++;
            y = (x % MAX_ROW_SIZE) == 0 ? y + 2 : y;
        }
    }

    /**
     * Update the displayed roster table of {@code player}. Inserts the newest
     * row into the table as known by the
     * @param player
     */
    private void updatePlyrTbl(String player) {
        JViewport viewport = plyrScrlls.get(player).getViewport();
        JTable tbl = (JTable)viewport.getView();
        var updatedRoster = drftHndlr.getPlayerRoster(player);
        int currRosterSize = updatedRoster.size();
        List<String> newestRiki = updatedRoster.get(currRosterSize-1);
        for (int i = 0; i < newestRiki.size(); i++) {
            String value = newestRiki.get(i);
            tbl.setValueAt(value, currRosterSize - 1, i);
        }
    }

    /**
     * Wrapper function to update the selection index of the {@code draftOrdrLst}.
     * Increments the selection index, unless the last index is selected, in
     * which case incrementing it would overflow
     */
    private void updateDrftOrdrLst() {
        int newIdx = draftOrdrLst.getSelectedIndex() + 1;
        if ( newIdx < draftOrdrLst.getMaxSelectionIndex() ) {
            draftOrdrLst.setSelectedIndex(newIdx);
        }
    }

    /**
     * Wrapper function to update the text display of {@code draftEvtLog}. Adds
     * the newest event to the top of the text area and
     */
    private void updateDrftEvtLog() {
        String oldStr = draftEvtLog.getText();
        String newStr = drftHndlr.getDraftEvent();
        draftEvtLog.setText(newStr + oldStr);
    }

    /**
     * Action Method for closing the Draft Dialog window
     *
     * Prompts user for confirmation on whether or not to close the window draft
     * dialog window, abandoning their draft.
     *
     * TODO : Allow user to save draft and pick up at another time
     * @param evt
     */
    private void actionDialogWindowClosing(java.awt.event.WindowEvent evt) {
        var pane = JOptionPane.showConfirmDialog(
            this,
            "Closing this window will prematurely end the draft,"
                + "would you like to cancel it?",
            "Cancel Draft?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (pane == JOptionPane.YES_OPTION) {
            drftHndlr.finishDraft();
            this.dispose();
        }
    }

    /**
     * Debug action, resets entire draft and removes all owners from the draftee
     * test table.
     *
     * TODO : Turn into an undo button or something of the sort
     * @param evt
     */
    private void actionResetActionPerformed(java.awt.event.ActionEvent evt) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Draft a rikishi selected in any one of the banzuke tables to the current
     * drafter's roster. Updates the corresponding drafter's roster table,
     * increments the selection of the draft order list, and adds the newest draft
     * event to the event log.
     *
     * @see DraftHandler
     * @param evt
     */
    private void actionSelectButActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
        if (!isSelection) {
            // TODO dialog warning for no selection
            return;
        }

        String actvDrftr = drftHndlr.getActiveDrafter();
        int draftResult = drftHndlr.draftRikishi(
            currSlctdCache.get("shikona")
            ,currSlctdCache.get("rank")
            ,Integer.valueOf(currSlctdCache.get("division")) );
        if (draftResult == -1) {
            throw new Exception("Wrestler has already been selected");
        }
        else if (draftResult == -2) {
            // TODO dialog warning for draft failure
            throw new Exception("Selection exceeds divisional amounts");
        }

        updatePlyrTbl(actvDrftr);
        updateDrftOrdrLst();
        updateDrftEvtLog();
    }

    /**
     * Checks whether currently selected rikishi in the {@code self} banzuke
     * draft table is available to be drafted, ie. not on someone elses team. If
     * the rikishi is available to be drafted, then the selection button is set
     * to be activated.
     *
     * Only checks if the rikishi is available, does *not* check if the rikishi
     * breaks the league division size settings
     * @param self the BanzTbl to be checked
     * @param evt
     */
    private void actionBanzTblMouseReleased(BanzDraftTbl self,
            java.awt.event.MouseEvent evt) {
        String name = self.getSelectedValue();
        if (name != null) {
            isSelection = true;

            if ( !name.equals(currSlctdCache.get("shikona")) ) {
                currSlctdCache.put("shikona", name);
                currSlctdCache.put("rank", self.getSelectedCol("Rank"));
                currSlctdCache.put("division", String.valueOf( self.getDivision()) );
                draftSlctBut.setEnabled(!drftHndlr.isRikishiDrafted(name));
            }
        }
        else {
            isSelection = false;
        }
    }

    // ==================================================================================
    // PRIVATE MEMBERS
    // ==================================================================================

    /**
     * Draft Handler
     */
    private DraftHandler drftHndlr;

    /**
     * Scroll Pane containing draft order
     */
    private javax.swing.JScrollPane draftOrdrScrll;

    /**
     * List containing and showing the draft order
     */
    private javax.swing.JList<String> draftOrdrLst;

    /**
     * Scroll pane containing the Event Log
     */
    private javax.swing.JScrollPane draftEvtLogScrll;

    /**
     * Reset button for the draft. Mostly for testing purposes
     */
    private javax.swing.JButton draftRstBut;

    /**
     * Draft Selection button for adding a rikishi to a team
     */
    private javax.swing.JButton draftSlctBut;

    /**
     * Label indicating the current drafter
     */
    private javax.swing.JLabel currDrafterLbl;

    /**
     * Event Log Display
     */
    private javax.swing.JTextArea draftEvtLog;

    /**
     * List of scroll panes containing the banzuke tables
     */
    private List<javax.swing.JScrollPane> banzScrlls;

    /**
     * Map of scroll panes containing the players tables mapped to their names
    */
    private Map<String, javax.swing.JScrollPane> plyrScrlls;

    /**
     * List of labels for each component in the player scrollpanes
     */
    private List<javax.swing.JLabel> plyrLbls;

    /**
     * Draft Tracking Panel for storing tracking elements of the draft
     */
    private javax.swing.JPanel trackingPnl;

    /**
     * Extra panel for storing the extra draft table
     */
    private javax.swing.JPanel banzPnl;

    /**
     * Extra Panel for storing tables for players
     */
    private javax.swing.JPanel plyrsPnl;

    /**
     * Checker for whether or not a selection is active
     */
    private boolean isSelection;

    /**
     * A map for caching information about the currently selected rikishi.
     * Meant to contain shikona, rank, and division
     */
    private Map<String, String> currSlctdCache;
}
