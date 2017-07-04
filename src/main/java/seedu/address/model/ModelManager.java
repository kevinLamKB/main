package seedu.address.model;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.EntryBookChangedEvent;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final EntryBook entryBook;
    private final FilteredList<ReadOnlyEntry> filteredEntries;

    /**
     * Initializes a ModelManager with the given entryBook and userPrefs.
     */
    public ModelManager(ReadOnlyEntryBook entryBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(entryBook, userPrefs);

        logger.fine("Initializing with entry book: " + entryBook + " and user prefs " + userPrefs);

        this.entryBook = new EntryBook(entryBook);
        filteredEntries = new FilteredList<>(this.entryBook.getEntryList());
    }

    public ModelManager() {
        this(new EntryBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyEntryBook newData) {
        entryBook.resetData(newData);
        indicateEntryBookChanged();
    }

    @Override
    public ReadOnlyEntryBook getEntryBook() {
        return entryBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateEntryBookChanged() {
        raise(new EntryBookChangedEvent(entryBook));
    }

    @Override
    public synchronized void deleteEntry(ReadOnlyEntry target) throws EntryNotFoundException {
        entryBook.removeEntry(target);
        indicateEntryBookChanged();
    }

    @Override
    public synchronized void addEntry(ReadOnlyEntry entry) {
        entryBook.addEntry(entry);
        updateFilteredListToShowAll();
        indicateEntryBookChanged();
    }

    @Override
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws EntryNotFoundException {
        requireAllNonNull(target, editedEntry);

        entryBook.updateEntry(target, editedEntry);
        indicateEntryBookChanged();
    }

    //=========== Filtered Entry List Accessors =============================================================

    /**
     * Return a list of {@code ReadOnlyEntry} backed by the internal list of {@code entryBook}
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList() {
        return new UnmodifiableObservableList<>(filteredEntries);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredEntries.setPredicate(null);
    }

    @Override
    public void updateFilteredEntryList(Set<String> keywords) {
        updateFilteredEntryList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredEntryList(Expression expression) {
        filteredEntries.setPredicate(expression::satisfies);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return entryBook.equals(other.entryBook)
                && filteredEntries.equals(other.filteredEntries);
    }

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyEntry entry);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyEntry entry) {
            return qualifier.run(entry);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyEntry entry);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyEntry entry) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(entry.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
