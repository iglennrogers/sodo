package model;


public class UserCommand extends Command {

    public UserCommand(final Cell cell, final CommandType type, final int value) {

        super(cell, type, value);
    }


    @Override
    public boolean is_user_command() {

        return true;
    }


    @Override
    public void execute() {

        throw new IllegalArgumentException();
    }


    @Override
    public void unexecute() {

        throw new IllegalArgumentException();
    }
}
