package seedu.address.storage;

import java.io.IOException;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Utility class for JsonAddressBook.
 */
public class JsonAddressBookUtil {

    private static final String MESSAGE_NULL_EMPTY_JSON_STRING = "JSON string cannot be null or empty";
    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookUtil.class);

    /**
     * Parses a JSON string into a ReadOnlyAddressBook.
     *
     * @param jsonString the JSON string representing the address book to parse
     * @return the ReadOnlyAddressBook parsed from the JSON string
     * @throws DataLoadingException if the JSON string is null, empty, contains
     *                              invalid JSON format,
     *                              or contains illegal field values
     */
    public static ReadOnlyAddressBook readAddressBookFromJsonString(String jsonString) throws DataLoadingException {
        if (StringUtil.isNullOrEmpty(jsonString)) {
            throw new DataLoadingException(new Exception(MESSAGE_NULL_EMPTY_JSON_STRING));
        }

        try {
            JsonSerializableAddressBook jsonAddressBook = JsonUtil.fromJsonString(jsonString,
                    JsonSerializableAddressBook.class);
            return jsonAddressBook.toModelType();
        } catch (IOException e) {
            logger.info("Failed to parse JSON string: " + e.getMessage());
            throw new DataLoadingException(e);
        } catch (IllegalValueException e) {
            logger.info("Illegal values found in JSON string: " + e.getMessage());
            throw new DataLoadingException(e);
        }
    }
}
