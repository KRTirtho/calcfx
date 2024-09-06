module calcfx {
    requires java.base;
    requires java.logging;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens calcfx to javafx.fxml;
    opens calcfx.controllers to javafx.fxml;

    exports calcfx;
    exports calcfx.controllers;
}