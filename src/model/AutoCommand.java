package model;


public class AutoCommand extends Command {

    public AutoCommand(final Cell cell, final CommandType type, final int value) {

        super(cell, type, value);
    }


    @Override
    public boolean is_user_command() {

        return false;
    }


    @Override
    public void execute() {

        if (_type == CommandType.AddPossibility) {

            _cell.add_possibility(_value);
        }
        else if (_type == CommandType.RemovePossibility) {

            _cell.remove_possibility(_value);
        }
        else {

            throw new IllegalArgumentException();
        }
    }


    @Override
    public void unexecute() {

        if (_type == CommandType.AddPossibility) {

            _cell.remove_possibility(_value);
        }
        else if (_type == CommandType.RemovePossibility) {

            _cell.add_possibility(_value);
        }
        else {

            throw new IllegalArgumentException();
        }
    }
}
