package Controllers;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Bubble extends Region {

    private final double arrowSize = 10;
    private final double borderRadius = 10;

    public Bubble(String text, boolean isUser) {
        setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Polygon bubble = new Polygon();
        bubble.setFill(isUser ? Color.LIGHTBLUE : Color.LIGHTGRAY);

        if (isUser) {
            bubble.getPoints().addAll(
                arrowSize, borderRadius,
                arrowSize, getHeight() - borderRadius,
                getWidth() - arrowSize, getHeight() - borderRadius,
                getWidth() - arrowSize, borderRadius,
                getWidth(), getHeight() / 2.0,
                getWidth() - arrowSize, 0.0,
                arrowSize, 0.0
            );
        } else {
            bubble.getPoints().addAll(
                arrowSize, borderRadius,
                arrowSize, getHeight() - borderRadius,
                getWidth() - arrowSize, getHeight() - borderRadius,
                getWidth() - arrowSize, arrowSize,
                getWidth(), getHeight() / 2.0,
                getWidth() - arrowSize, 0.0,
                arrowSize, 0.0
            );
        }

        getChildren().add(bubble);
    }
}

