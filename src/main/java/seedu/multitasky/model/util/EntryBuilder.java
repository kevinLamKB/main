package seedu.multitasky.model.util;

import java.util.Objects;
import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

//@@author A0125586X
/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_NAME = "defaultName";
    public static final String DEFAULT_TAGS = "defaultTag";

    private String name;
    private Set<Tag> tags;
    private Calendar startDateAndTime = null;
    private Calendar endDateAndTime = null;

    private Entry entry;

    public EntryBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.entry = new FloatingTask(defaultName, tags);
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     * {code entryToCopy} cannot be null.
     */
    public EntryBuilder(ReadOnlyEntry entryToCopy) {
        Objects.requireNonNull(entryToCopy);
        if (entryToCopy instanceof Event) {
            this.entry = new Event(entryToCopy);
        } else if (entryToCopy instanceof Deadline) {
            this.entry = new Deadline(entryToCopy);
        } else {
            assert (entryToCopy instanceof FloatingTask);
            this.entry = new FloatingTask(entryToCopy);
        }
    }

    public EntryBuilder withName(String name) throws IllegalValueException {
        this.name = name;
        this.entry.setName(new Name(name));
        return this;
    }

    public EntryBuilder withTags(String... tags) throws IllegalValueException {
        this.tags = SampleDataUtil.getTagSet(tags);
        this.entry.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public EntryBuilder withTags(Set<Tag> tags) throws IllegalValueException {
        this.tags = tags;
        this.entry.setTags(tags);
        return this;
    }

    public EntryBuilder withStartDateAndTime(Calendar startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
        return this;
    }

    public EntryBuilder withEndDateAndTime(Calendar endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
        return this;
    }

    public Entry build() throws Exception {
        if (endDateAndTime == null) {
            entry = new FloatingTask(new Name(name), tags);
        } else if (startDateAndTime == null) {
            entry = new Deadline(new Name(name), endDateAndTime, tags);
        } else {
            entry = new Event(new Name(name), startDateAndTime, endDateAndTime, tags);
        }
        return entry;
    }

    // @@author A0126623L
    /**
     * Builds an appropriate entry (i.e. Event, Deadline, FloatingTask, ...) based on the given argument.
     *
     * @return
     * @throws IllegalValueException
     */

    public Entry build(Name name, Calendar startDateAndTime, Calendar endDateAndTime, Set<Tag> tags)
            throws IllegalValueException {

        if (startDateAndTime == null) {
            // Floating task
            if (endDateAndTime == null) {
                return new FloatingTask(name, tags);
                // Deadline
            } else {
                return new Deadline(name, endDateAndTime, tags);
            }
            // Event
        } else if (endDateAndTime != null) {
            return new Event(name, startDateAndTime, endDateAndTime, tags);
            // Unknown combination of present start date but no end date
        } else {
            assert false : "Error in EntryBuilder.";
            return null;
        }
    }
    // @@author

}
