package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {
	
	private Stage loginStage;
	private boolean logged = false;
	
	@FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Label messageLabel;
    @FXML private Button loginButton;
    
    public LoginController() {
	}


	public void setLoginStage(Stage loginStage) {
		// TODO Auto-generated method stub
		this.loginStage = loginStage;
	}

	public boolean esitoLogin() {
		// TODO Auto-generated method stub
		return logged;
	}
	
	@FXML
    public void click() {
		if (usernameTextField.getText() == null || usernameTextField.getText().length() == 0)
			messageLabel.setText("Inserisci username");
		else if (passwordPasswordField.getText() == null || passwordPasswordField.getText().length() == 0)
			messageLabel.setText("Inserisci password");
		else if (!check(usernameTextField.getText(), passwordPasswordField.getText()))
			messageLabel.setText("Username o password errati");
		else {
			logged=true;
			loginStage.close();
		}
    }

    private boolean check(String username, String password) {
		return username.equals(password);
	}

	@FXML
    public void keyboardEnterPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			loginButton.fire();
    }

}
