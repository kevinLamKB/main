package seedu.address.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * Panel containing the list of entries.
 */
public class EntryListPanel extends UiPart<Region> {
    private static final String FXML = "EntryListPanel.fxml";

    @FXML
    private ListView<ReadOnlyEntry> entryListView;

    public EntryListPanel(ObservableList<ReadOnlyEntry> entryList) {
        super(FXML);
        setConnections(entryList);
    }

    private void setConnections(ObservableList<ReadOnlyEntry> entryList) {
        entryListView.setItems(entryList);
        entryListView.setCellFactory(listView -> new EntryListViewCell());
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            entryListView.scrollTo(index);
            entryListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class EntryListViewCell extends ListCell<ReadOnlyEntry> {

        @Override
        protected void updateItem(ReadOnlyEntry entry, boolean empty) {
            super.updateItem(entry, empty);

            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new EntryCard(entry, getIndex() + 1).getRoot());
            }
        }
    }

}
