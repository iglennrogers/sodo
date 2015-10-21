package model;


public abstract class Command {

    public enum CommandType {

        Reset, SetKnown, AddPossibility, RemovePossibility
    }


    public Command(final Cell cell, final CommandType type, final int value) {

        _cell = cell;
        _type = type;
        _value = value;
    }


    public CommandType type() {

        return _type;
    }


    public Cell cell() {

        return _cell;
    }


    public int value() {

        return _value;
    }


    @Override
    public String toString() {

        return "Command: " + _type.toString() + " Cell: " + _cell.toString()
                + " Value: " + ((Integer) _value).toString();
    }


    public abstract boolean is_user_command();


    public abstract void execute();


    public abstract void unexecute();


    protected final Cell _cell;
    protected final CommandType _type;
    protected final int _value;
}
