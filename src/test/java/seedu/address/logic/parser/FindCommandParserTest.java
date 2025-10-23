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
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice", "Bob"), List.of(), null, 
                        List.of(), List.of()));
        assertParseSuccess(parser, " n:Alice Bob", expectedFindCommand);
    }

    @Test
    public void parse_validTagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), Arrays.asList("big-spender", "colleague"), null, 
                        List.of(), List.of()));
        assertParseSuccess(parser, " t:big-spender colleague", expectedFindCommand);
    }

    @Test
    public void parse_validBothPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice"), Arrays.asList("colleague"), null, 
                        List.of(), List.of()));
        assertParseSuccess(parser, " n:Alice t:colleague", expectedFindCommand);
    }

    @Test
    public void parse_validStatusPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), "uncontacted", List.of(), List.of()));
        assertParseSuccess(parser, " s:uncontacted", expectedFindCommand);
    }

    @Test
    public void parse_validAllPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice"), Arrays.asList("colleague"), "contacted", 
                        List.of(), List.of()));
        assertParseSuccess(parser, " n:Alice t:colleague s:contacted", expectedFindCommand);
    }

    @Test
    public void parse_validPhonePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), null, Arrays.asList("91234567"), List.of()));
        assertParseSuccess(parser, " p:91234567", expectedFindCommand);
    }

    @Test
    public void parse_validEmailPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), null, List.of(), 
                        Arrays.asList("alice@example.com")));
        assertParseSuccess(parser, " e:alice@example.com", expectedFindCommand);
    }

    @Test
    public void parse_validPhoneAndEmailPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(List.of(), List.of(), null, Arrays.asList("91234567"), 
                        Arrays.asList("alice@example.com")));
        assertParseSuccess(parser, " p:91234567 e:alice@example.com", expectedFindCommand);
    }

    @Test
    public void parse_validNameAndPhonePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonMatchesKeywordsPredicate(Arrays.asList("Alice"), List.of(), null, 
                        Arrays.asList("91234567"), List.of()));
        assertParseSuccess(parser, " n:Alice p:91234567", expectedFindCommand);
    }

}
