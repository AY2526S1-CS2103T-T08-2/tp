package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice")));
        assertParseSuccess(parser, "Alice", expectedFindCommand);
    }

    @Test
    public void parse_validNamePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice", "Bob"), List.of()));
        assertParseSuccess(parser, " n:Alice Bob", expectedFindCommand);
    }

    @Test
    public void parse_validTagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), Arrays.asList("big-spender", "colleague")));
        assertParseSuccess(parser, " t:big-spender colleague", expectedFindCommand);
    }

    @Test
    public void parse_validBothPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice"), Arrays.asList("colleague")));
        assertParseSuccess(parser, " n:Alice t:colleague", expectedFindCommand);
    }
}
