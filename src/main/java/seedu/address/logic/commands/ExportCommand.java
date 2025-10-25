package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.clipboard.ClipboardProvider;
import seedu.address.logic.clipboard.SystemClipboardProvider;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.files.FileSystemProvider;
import seedu.address.logic.files.SystemFileSystemProvider;
import seedu.address.model.Model;
import seedu.address.storage.JsonAddressBookUtil;

/**
 * Exports the contents of the user's address book JSON file to the clipboard.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_SUCCESS = "Copied address book data to clipboard.";
    public static final String MESSAGE_FILE_MISSING = "Export failed: Address book file not found.";
    public static final String MESSAGE_FILE_READ_ERROR = "Export failed: Could not read address book file.";
    public static final String MESSAGE_INVALID_JSON = "Export failed: Stored file contains invalid JSON.";
    public static final String MESSAGE_FILEPATH_NOT_CONFIGURED = "Export failed: File path not configured.";

    private final ClipboardProvider clipboardProvider;
    private final FileSystemProvider fileSystemProvider;

    public ExportCommand() {
        this(new SystemClipboardProvider(), new SystemFileSystemProvider());
    }

    public ExportCommand(ClipboardProvider clipboardProvider) {
        this(clipboardProvider, new SystemFileSystemProvider());
    }

    /**
     * This constructor is primarily intended for testing, where a fake clipboard
     * and fake filesystem can be provided to avoid side effects.
     *
     * @param clipboardProvider provider used to place the exported JSON on the clipboard
     * @param fileSystemProvider provider used to check for file existence and read file contents
     * @throws NullPointerException if any argument is {@code null}
     */
    public ExportCommand(ClipboardProvider clipboardProvider, FileSystemProvider fileSystemProvider) {
        this.clipboardProvider = requireNonNull(clipboardProvider);
        this.fileSystemProvider = requireNonNull(fileSystemProvider);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Path filePath = model.getAddressBookFilePath();
        if (filePath == null) {
            throw new CommandException(MESSAGE_FILEPATH_NOT_CONFIGURED);
        }

        if (!fileSystemProvider.exists(filePath)) {
            throw new CommandException(MESSAGE_FILE_MISSING);
        }

        try {
            String content = fileSystemProvider.readFile(filePath);
            if (content == null || content.trim().isEmpty()) {
                throw new CommandException(MESSAGE_INVALID_JSON);
            }
            JsonAddressBookUtil.readAddressBookFromJsonString(content);

            clipboardProvider.setString(content);

            return new CommandResult(MESSAGE_SUCCESS);
        } catch (DataLoadingException e) {
            throw new CommandException(MESSAGE_INVALID_JSON, e);
        } catch (IOException e) {
            throw new CommandException(MESSAGE_FILE_READ_ERROR, e);
        }
    }
}
