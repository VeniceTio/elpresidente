package view;

import controller.ElementControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class GameView {
    /**
     * Instance de la vue
     */
    private static GameView _instance = null;
    /**
     * Fenêtre
     */
    private static Stage _stage = null;

    /**
     * Méthode renvoyant l'instance de la vue
     * @return L'instance de la vue
     */
    public static GameView getInstance() {
        if(_instance == null)
            _instance = new GameView();

        return _instance;
    }

    /**
     * Méthode permettant de construire puis afficher la vue
     * @param playerName Nom du joueur ayant lancé la partie
     */
    public void start(String playerName) throws Exception {
        // Chargement de toutes les ressources nécessaires (polices et scène)
        Font.loadFont(getClass().getResourceAsStream("../resources/fonts/Cocogoose.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("../resources/fonts/Roboto-Regular.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("../resources/fonts/Roboto-Bold.ttf"), 16);
        Parent p = FXMLLoader.load(getClass().getResource("../resources/fxml/game_scene.fxml"));

        // On récupère les différents conteneurs principaux pour construire la fenêtre ensuite
        VBox root = (VBox) p;
        AnchorPane header = (AnchorPane) root.getChildren().get(0); // AnchorPane id "header"
        AnchorPane availableMoneyPane = (AnchorPane) header.getChildren().get(4); // AnchorPane id "available-money-pane"
        AnchorPane container = (AnchorPane) root.getChildren().get(1); // AnchorPane id "container"
        AnchorPane indicatorsPane = (AnchorPane) container.getChildren().get(0); // AnchorPane id "indicators"
        AnchorPane leversPane = (AnchorPane) container.getChildren().get(1); // AnchorPane id "levers"

        // Remplacement du nom du joueur en fonction du nom entré précedemment
        ((Text)header.getChildren().get(3)).setText(playerName); // Text id "player-name"

        // Ajout de l'image pour le détail du scénario (en bas à droite)
        ImageView mission = (ImageView)container.getChildren().get(2);
        if(ElementControl.getInstance().getEnd() instanceof RepRecEnd8) {
            mission.setImage(new Image("file:src/resources/images/mission_objective.png"));
        } else if(ElementControl.getInstance().getEnd() instanceof DeadEnd) {
            mission.setImage(new Image("file:src/resources/images/mission_death.png"));
        } else {
            mission.setImage(new Image("file:src/resources/images/mission_none.png"));
        }


        // Génération dynamique des leviers et indicateurs
        double offsetX = 0;
        double offsetY = 0;
        ElementControl ec = ElementControl.getInstance();

        // Génération des leviers
        for(LeverFamily lf : ec.getFamilyLevers()) {
            Text famNameText = new Text(lf.getName().toUpperCase());
            DropShadow ds = new DropShadow();
            ds.setRadius(0);
            ds.setOffsetX(-2.0f);
            ds.setColor(Color.rgb(231, 63, 8));
            famNameText.setEffect(ds);
            famNameText.setFont(new Font("Cocogoose", 32));
            famNameText.setFill(Color.WHITE);
            famNameText.setY(offsetY);
            leversPane.getChildren().add(famNameText);
            offsetY += 20;
            for(Lever l : lf.getLevers()) {
                if(l.get_name() != "rValorisation") {
                    Pane lcp = new LeverController(l.get_name()).getPane();
                    lcp.setTranslateX(offsetX);
                    lcp.setTranslateY(offsetY);
                    offsetX += 250;
                    leversPane.getChildren().add(lcp);

                    if (offsetX > 3 * 250) {
                        offsetX = 0;
                        offsetY += 75;
                    }
                }
            }
            offsetY += 150;
            offsetX = 0;
        }

        // Génération des indicateurs
        offsetX = 0;
        offsetY = 0;
        String[] hiddenIndicators = {"revenus des inscriptions", "valorisation batiment", "valorisation bien", "subventions de l'état"};
        ArrayList<String> hiddenIndicatosrAL = new ArrayList<String>(Arrays.asList(hiddenIndicators));
        for(Indicator ind : ElementControl.getInstance().getIndicators()) {
            if(!hiddenIndicatosrAL.contains(ind.get_name())) {
                String indicatorName = ind.get_name();
                Pane indicatorText = new IndicatorText(indicatorName).getPane();

                if(indicatorName == "argent disponible") {
                    indicatorText.setTranslateX(70);
                    indicatorText.setTranslateY(16);
                    availableMoneyPane.getChildren().add(indicatorText);
                } else {
                    indicatorText.setTranslateX(offsetX);
                    indicatorText.setTranslateY(offsetY);
                    indicatorsPane.getChildren().add(indicatorText);
                    offsetX += 280;

                    if(offsetX > 2*350) {
                        offsetX = 0;
                        offsetY += 100;
                    }
                }
            }
        }

        // Affichage du nombre de semestres écoulés
        AnchorPane footer = (AnchorPane) root.getChildren().get(2);
        Pane semesterPane = (Pane) footer.getChildren().get(1); // Pane id semester
        SemesterText semesterText = new SemesterText(String.valueOf(Semestre.getInstance().getSemestre()));
        Semestre.getInstance().add(semesterText);
        semesterPane.getChildren().add(semesterText);

        // Paramètrage de la fenêtre puis affichage
        _stage = new Stage();
        _stage.initStyle(StageStyle.UNDECORATED);
        _stage.setResizable(false);
        _stage.setTitle("El Presidente");

        _stage.setScene(new Scene(p));
        _stage.setFullScreen(true);
        _stage.show();
    }

    /**
     * Méthode permettant de passer à l'écran de fin de partie
     */
    public void goToEndView(boolean win) throws Exception {
        _stage.close();
        EndView.getInstance().start(win);
    }
}
