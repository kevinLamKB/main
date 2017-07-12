package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.commons.util.StringUtil;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces
     * will be trimmed.
     *
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new Name(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Calendar>} if {@code name} is present.
     */
    public static Optional<Calendar> parseDate(Optional<String> inputArgs) throws IllegalValueException {
        requireNonNull(inputArgs);
        return inputArgs.isPresent() ? Optional.of(DateUtil.stringToCalendar(inputArgs.get(), null)) : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagString : tags) {
            // @@author A0140633R
            for (String tagName : tagString.split("\\s+")) {
                tagSet.add(new Tag(tagName));
            }
            // @@author
        }
        return tagSet;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean areAllPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    // @@author A0140633R
    /**
     * Returns true if any of the prefixes contain non-empty values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Filters out Prefix's not mapped to anything in {@argMultimap}, and returns prefix that has arguments mapped
     * to it.
     *
     * Precondition: 1 and only 1 Prefix of the given argument prefixes have arguments mapped to it.
     */
    public static Prefix getDatePrefix(ArgumentMultimap argMultimap, Prefix... prefixes) {
        List<Prefix> temp = Stream.of(prefixes).filter(prefix -> argMultimap.getValue(prefix).isPresent())
                                  .collect(Collectors.toList());
        assert (temp.size() <= 1) : "invalid flag combination not catched beforehand or no Prefixes found!";
        return temp.get(0);
    }

}
