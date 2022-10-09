package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RECORDS;

/**
 * Lists all records for a specific patient to the user.
 */
public class ListRCommand extends Command{

    public static final String COMMAND_WORD = "listR";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a list of all records for the specific patient in the records database.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed records for this patient: ";

    private final Index targetIndex;

    private static Person lastCalledPerson;

    public ListRCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Checks if listR command is previously called.
     *
     * @return true if listR command is previously called, false otherwise.
     */
    public static boolean isCalled() {
        return ListRCommand.lastCalledPerson != null;
    }

    public static Person getLastCalledPerson() {
        return ListRCommand.lastCalledPerson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToListRecords = lastShownList.get(targetIndex.getZeroBased());

        ListRCommand.lastCalledPerson = personToListRecords;

        model.setFilteredRecordList(personToListRecords);
        model.updateFilteredRecordList(PREDICATE_SHOW_ALL_RECORDS);
        StringBuilder builder = new StringBuilder()
                .append(MESSAGE_SUCCESS)
                .append(personToListRecords.getName())
                .append("\n")
                .append(Messages.MESSAGE_RECORDS_LISTED_OVERVIEW)
                .append(model.getFilteredRecordList().size());

        return new CommandResult(builder.toString(), false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListRCommand // instanceof handles nulls
                && targetIndex.equals(((ListRCommand) other).targetIndex)); // state check
    }
}
