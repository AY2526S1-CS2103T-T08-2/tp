package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG, PREFIX_STATUS);

        String preamble = map.getPreamble();

        boolean hasName = arePrefixesPresent(map, PREFIX_NAME);
        boolean hasTag = arePrefixesPresent(map, PREFIX_TAG);
        boolean hasStatus = arePrefixesPresent(map, PREFIX_STATUS);

        // Non-prefixed mode: no prefixes, use preamble as name keywords
        if (!hasName && !hasTag && !hasStatus) {
            String trimmed = preamble.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            List<String> keywords = Arrays.asList(trimmed.split("\\s+"));
            return new FindCommand(new NameContainsKeywordsPredicate(keywords));
        }

        // Prefixed mode: allow either or both prefixes, but preamble must be empty
        if (!preamble.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Get name keywords
        List<String> nameKeywords = map.getValue(PREFIX_NAME)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split("\\s+")))
                .orElse(List.of());

        // Get tag keywords
        List<String> tagKeywords = map.getValue(PREFIX_TAG)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split("\\s+")))
                .orElse(List.of());

        // Get status keyword
        String statusKeyword = map.getValue(PREFIX_STATUS)
                .map(String::trim)
                .orElse(null);


        return new FindCommand(new PersonMatchesKeywordsPredicate(nameKeywords, tagKeywords, statusKeyword));
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
