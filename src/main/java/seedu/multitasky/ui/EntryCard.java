package seedu.multitasky.ui;

import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0125586X
/**
 * EntryCard implements a superclass for the different types of entry cards to inherit from.
 * This class is declared as abstract as the classes for each type of entry card
 * should be instantiated instead of a generic entry card.
 */
public abstract class EntryCard extends UiPart<Region> {

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    protected HBox cardPane;
    @FXML
    protected Label name;
    @FXML
    protected Label id;
    @FXML
    protected Label startDateTime;
    @FXML
    protected Label endDateTime;
    @FXML
    protected Label additionalInfo;
    @FXML
    protected FlowPane tags;

    protected PrettyTime prettyTime;

    public EntryCard(String fxml, ReadOnlyEntry entry, int displayedIndex) {
        super(fxml);
        initAll(entry, displayedIndex);
        prettyTime = new PrettyTime();
    }

    protected void initAll(ReadOnlyEntry entry, int displayedIndex) {
        initNameId(entry, displayedIndex);
        initTags(entry);
    }

    protected void initNameId(ReadOnlyEntry entry, int displayedIndex) {
        name.setText(entry.getName().toString());
        id.setText(displayedIndex + ". ");
    }

    protected void initTags(ReadOnlyEntry entry) {
        entry.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    protected String prettyTimeFormatDate(Date date) {
        return prettyTime.format(date);
    }
}
