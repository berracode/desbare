package com.ritallus.desvare.ui.controller.commands.gpg;

import com.ritallus.desvare.core.domain.enums.ExpirationUnit;
import com.ritallus.desvare.core.domain.enums.KeyType;
import com.ritallus.desvare.ui.viewmodel.gpg.CreateKeyViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateKeyController {

    private final CreateKeyViewModel createKeyViewModel;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassphrase;

    @FXML
    private RadioButton rbRsaRsa;

    @FXML
    private RadioButton rbDsaElgamal;

    @FXML
    private RadioButton rbDsaSignOnly;

    @FXML
    private RadioButton rbRsaSignOnly;

    @FXML
    private RadioButton rbExistingKey;

    @FXML
    private ComboBox<Integer> cbKeySize;

    @FXML
    private CheckBox chkNeverExpire;

    @FXML
    private ComboBox<Integer> cbExpireAmount;

    @FXML
    private ComboBox<ExpirationUnit> cbExpireUnit;

    @FXML
    private Label lblStatus;


    public CreateKeyController(CreateKeyViewModel createKeyViewModel) {
        this.createKeyViewModel = createKeyViewModel;
    }

    @FXML
    public void initialize() {

        log.info("Initialize CreateKeyController");

        txtName.textProperty()
                .bindBidirectional(createKeyViewModel.getName());

        txtEmail.textProperty()
                .bindBidirectional(createKeyViewModel.getEmail());

        txtPassphrase.textProperty()
                .bindBidirectional(createKeyViewModel.getPassphrase());

        lblStatus.textProperty()
                .bind(createKeyViewModel.getStatus());

        cbKeySize.valueProperty()
                .bindBidirectional(
                        createKeyViewModel.getKeySize()
                );

        chkNeverExpire.selectedProperty()
                .bindBidirectional(
                        createKeyViewModel.getNeverExpire()
                );

        cbExpireAmount.valueProperty()
                .bindBidirectional(
                        createKeyViewModel.getExpirationAmount()
                );

        cbExpireUnit.valueProperty().bindBidirectional(
                createKeyViewModel.getExpirationUnit()
        );

        rbRsaRsa.selectedProperty().addListener(
                (obs, oldValue, selected) -> {
                    if (selected) {
                        createKeyViewModel
                                .getKeyType()
                                .set(KeyType.RSA_RSA);
                    }
                }
        );

        ToggleGroup keyTypeGroup = new ToggleGroup();

        rbRsaRsa.setToggleGroup(keyTypeGroup);
        rbDsaElgamal.setToggleGroup(keyTypeGroup);
        rbDsaSignOnly.setToggleGroup(keyTypeGroup);
        rbRsaSignOnly.setToggleGroup(keyTypeGroup);
        rbExistingKey.setToggleGroup(keyTypeGroup);

        cbKeySize.getItems().addAll(
                1024,
                2048,
                3072,
                4096
        );

        cbKeySize.setValue(4096);

        cbExpireAmount.getItems().addAll(
                1, 2, 3, 4, 5, 6, 7, 8, 9
        );

        cbExpireAmount.setValue(1);

        cbExpireUnit.getItems().addAll(
                ExpirationUnit.WEEKS,
                ExpirationUnit.MONTHS,
                ExpirationUnit.YEARS
        );

        cbExpireUnit.setValue(ExpirationUnit.YEARS);

        cbExpireAmount.disableProperty()
                .bind(chkNeverExpire.selectedProperty());

        cbExpireUnit.disableProperty()
                .bind(chkNeverExpire.selectedProperty());
    }

    @FXML
    private void handleGenerateKey() {

        createKeyViewModel.generate();
    }

}