package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.clipboard.ClipboardProvider;
import seedu.address.logic.clipboard.SystemClipboardProvider;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookUtil;

/**
 * Imports address book data from the user's clipboard.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_SUCCESS = "Imported address book data from clipboard.";
    public static final String MESSAGE_EMPTY_CLIPBOARD = "Clipboard does not contain any text to import.";
    public static final String MESSAGE_INVALID_JSON = "Failed to import: "
            + "Clipboard does not contain valid address book JSON.";

    private final ClipboardProvider clipboardProvider;

    /**
     * Creates an ImportCommand using the system clipboard.
     */
    public ImportCommand() {
        this(new SystemClipboardProvider());
    }

    /**
     * Creates an ImportCommand with a custom clipboard provider.
     * Not really needed in actual code, mainly for testing.
     */
    public ImportCommand(ClipboardProvider clipboardProvider) {
        this.clipboardProvider = requireNonNull(clipboardProvider);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        String text = clipboardProvider.getString();
        if (text == null || text.trim().isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_CLIPBOARD);
        }

        try {
            ReadOnlyAddressBook addressBook = JsonAddressBookUtil.readAddressBookFromJsonString(text);
            model.setAddressBook(addressBook);
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (DataLoadingException e) {
            throw new CommandException(MESSAGE_INVALID_JSON, e);
        } catch (RuntimeException e) {
            throw new CommandException("Unexpected error during import.", e);
        }
    }
}
