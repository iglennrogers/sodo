package view;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.Cell;
import model.Command;
import model.UserCommand;
import sodo.Sodo;


@SuppressWarnings("serial")
class CellAction extends AbstractAction {

    CellAction(final Sodo controller, final Cell cell, final String v) {

        super(v);
        _cell = cell;
        _controller = controller;
    }


    public void actionPerformed(final ActionEvent arg) {

        final String number = arg.getActionCommand();
        final Command.CommandType action = (Command.CommandType) getValue("action");

        final int value = (action != Command.CommandType.Reset) ? Integer
                .valueOf(number) : 0;
        _controller.execute_user_command(new UserCommand(_cell, action, value));
    }


    private final Cell _cell;
    private final Sodo _controller;
}
