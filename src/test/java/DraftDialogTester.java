/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.sumoproject.DraftDialog;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author blarg
 */
public class DraftDialogTester {
    public static void main(String[] args) {
        DraftDialog dd = new DraftDialog(
            null,
            null,
            List.of("Drafter1", "Drafter2"),
            new int[]{1},
            true,
            "Linear"
        );
        dd.pack();
        dd.setVisible(true);
    }
         
}
