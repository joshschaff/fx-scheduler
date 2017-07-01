package Elements;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by josh on 6/12/17.
 */
public class DialogHelp {

    public static String getStringDialog(String start, String title, String header, String content) {
        String result = "";
        while (result.equals("")) {


            TextInputDialog dialog = new TextInputDialog(start);
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            dialog.setContentText(content);

            Optional<String> diagResult = dialog.showAndWait();

            if (diagResult.isPresent()){
                result = diagResult.get();
            }
        }
        return result;
    }

    public static int getIntDialog(int max, String title, String headerText, String contentText) {
        int result = 0;
        while (result == 0) {

            ArrayList<Integer> choices = new ArrayList<>();
            IntStream.range(1, max+1).forEach(choices::add);

            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle(title);
            dialog.setHeaderText(headerText);
            dialog.setContentText(contentText);

            Optional<Integer> diagResult = dialog.showAndWait();

            if (diagResult.isPresent()){
                result = diagResult.get();
            }
        }
        return result-1;
    }
}
