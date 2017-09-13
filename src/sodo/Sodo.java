package sodo;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.Stack;

import model.AutoCommand;
import model.Board;
import model.Cell;
import model.Command;
import model.Command.CommandType;
import model.UserCommand;
import view.MainWindow;


public class Sodo {

    public static void main(final String[] args) {

        new Sodo().run();
    }


    public Sodo() {

        _view = new MainWindow(this);
    }


    public void run() {

    }


    public void exit() {

        _view.close();
    }


    public void newGame() {

        _view.newGame();
        _board.reset();
    }


    public void loadGame() {

        newGame();

        BufferedReader fp;
        try {

            fp = new BufferedReader(new FileReader(
                    "C:\\Users\\g_rogers\\Desktop\\soko.txt"));
            for (int row = 0; row < Board.SIZE; row++) {

                final String line = fp.readLine();
                for (int col = 0; col < Board.SIZE; col++) {

                    final int num = Integer.parseInt(line.substring(col,
                            col + 1));
                    if (num != 0) {

                        final Cell cell = _board.cell(row, col);
                        execute_user_command(new UserCommand(cell,
                                CommandType.SetKnown, num));
                    }
                }
            }
            fp.close();
        }
        catch (final FileNotFoundException e) {

            e.printStackTrace();
        }
        catch (final IOException e) {

            e.printStackTrace();
        }
    }


    public void undo() {

        if (_undo_stack.isEmpty()) {

            return;
        }

        while (true) {

            final Command cmd = _undo_stack.pop();
            if (cmd.is_user_command()) {

                break;
            }
            cmd.unexecute();
        }
    }


    public Cell cell(final int row, final int col) {

        return _board.cell(row, col);
    }


    public void execute_user_command(final UserCommand command) {

        _undo_stack.push(command);

        switch (command.type()) {

        case AddPossibility:
            push_auto_command(new AutoCommand(command.cell(), command.type(),
                    command.value()));
            break;
        case RemovePossibility:
            push_auto_command(new AutoCommand(command.cell(), command.type(),
                    command.value()));
            break;
        case SetKnown:
            push_auto_command(new AutoCommand(command.cell(), command.type(),
                    command.value()));
            break;
        case Reset:
            final Set<Integer> poss = command.cell().get_possibilities();
            for (int num = 1; num <= Board.SIZE; num++) {

                if (!poss.contains(num)) {
                    push_auto_command(new AutoCommand(command.cell(),
                            Command.CommandType.AddPossibility, num));
                }
            }
            break;
        default:
            break;
        }

        final int loops = 0;
        while (loops < 80) {

            final AutoCommand cmd = _board.simplify();
            if (cmd == null) {

                break;
            }
            push_auto_command(cmd);
        }
    }


    public void push_auto_command(final AutoCommand command) {

        if (command.type() == Command.CommandType.SetKnown) {

            final Set<Integer> poss = command.cell().get_possibilities();

            for (final int num : poss) {

                if (num != command.value()) {

                    push_auto_command(new AutoCommand(command.cell(),
                            Command.CommandType.RemovePossibility, num));
                }
            }
            return;
        }

        _undo_stack.push(command);
        command.execute();
    }


    private final Board _board = new Board();
    private final Stack<Command> _undo_stack = new Stack<>();
    private final MainWindow _view;
}
