package view;


import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import model.Board;
import model.Cell;
import sodo.Sodo;


public class MainWindow {

    public MainWindow(final Sodo sodo) {

        this._controller = sodo;

        final JFrame f = new JFrame();
        f.setTitle("Sudoku");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JMenuBar menu = new JMenuBar();
        menu.add(createMenu());
        f.setJMenuBar(menu);

        final JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.LINE_AXIS));

        _number_panel = new NumberPanel(this);
        contents.add(_number_panel);

        contents.add(createGamePanel());

        _show_numbers_panel = new ShowPanel(this);
        contents.add(_show_numbers_panel);

        f.setContentPane(contents);
        f.pack();
        f.setVisible(true);

        newGame();
    }


    private JPanel createGamePanel() {

        final JPanel contents = new JPanel();
        contents.setLayout(new GridLayout(Board.ROOTSIZE, Board.ROOTSIZE, 3, 3));
        contents.setBackground(Color.BLUE);
        contents.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

        for (int sq = 0; sq < Board.SIZE; sq++) {

            final JPanel sqcontents = new JPanel();
            sqcontents.setLayout(new GridLayout(Board.ROOTSIZE, Board.ROOTSIZE));
            sqcontents.setBackground(Color.BLUE);
            sqcontents.setBorder(BorderFactory.createLineBorder(Color.RED));
            for (int sqy = 0; sqy < Board.ROOTSIZE; sqy++) {

                for (int sqx = 0; sqx < Board.ROOTSIZE; sqx++) {

                    final int x = ((sq % Board.ROOTSIZE) * Board.ROOTSIZE) + sqx;
                    final int y = ((sq / Board.ROOTSIZE) * Board.ROOTSIZE) + sqy;
                    final Cell cell = _controller.cell(y, x);
                    final CellView p = new CellView(_controller, this, cell);
                    sqcontents.add(p);
                }
            }
            contents.add(sqcontents);
        }

        return contents;
    }


    @SuppressWarnings("serial")
    private JMenu createMenu() {

        final JMenu menu = new JMenu("Game");

        {
            final Action actionNew = new AbstractAction("New") {

                public void actionPerformed(final ActionEvent arg0) {

                    _controller.newGame();
                }
            };
            actionNew.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
            actionNew.putValue(Action.SHORT_DESCRIPTION, "New game");
            menu.add(actionNew);
        }
        {
            final Action actionUndo = new AbstractAction("Undo") {

                public void actionPerformed(final ActionEvent arg0) {

                    _controller.undo();
                }
            };
            actionUndo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
            actionUndo.putValue(Action.SHORT_DESCRIPTION, "Undo last move");
            menu.add(actionUndo);
        }
        {
            final Action actionNew = new AbstractAction("Load") {

                public void actionPerformed(final ActionEvent arg0) {

                    _controller.loadGame();
                }
            };
            actionNew.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
            actionNew.putValue(Action.SHORT_DESCRIPTION, "Load game");
            menu.add(actionNew);
        }
        menu.add(new JSeparator());
        {
            final Action actionExit = new AbstractAction("Exit") {

                public void actionPerformed(final ActionEvent arg0) {

                    _controller.exit();
                }
            };
            actionExit.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
            actionExit.putValue(Action.SHORT_DESCRIPTION, "Exit the game");
            menu.add(actionExit);
        }

        return menu;
    }


    public void newGame() {

        _show_numbers_panel.reset();
        _number_panel.reset();
        set_selection(null);
    }


    public void close() {

        for (final Frame frame : Frame.getFrames()) {

            frame.dispose();
        }
    }


    NumberPanel number_panel() {

        return _number_panel;
    }


    void register_highlight(final int value, final boolean check) {

        _checks[value] = check;
    }


    void register_pair_highlight(final boolean check) {

        _show_pairs = check;
    }


    void register_triplet_highlight(final boolean check) {

        _show_triplets = check;
    }


    boolean has_highlight(final int value) {

        return _checks[value] || _show_pairs || _show_triplets;
    }


    boolean show_pairs() {

        return _show_pairs;
    }


    boolean show_triplets() {

        return _show_triplets;
    }


    boolean any_highlight() {

        boolean result = true;
        for (int i = 1; i < _checks.length; ++i) {

            result &= _checks[i];
        }
        return (!result) || _show_pairs || _show_triplets;
    }


    void set_selection(final CellView cell) {

        if (_selected_cell != null) {

            _selected_cell.set_selected(false);
        }
        _selected_cell = cell;
        if (_selected_cell != null) {

            _selected_cell.set_selected(true);
        }
    }


    private final Sodo _controller;
    private final ShowPanel _show_numbers_panel;
    private final NumberPanel _number_panel;
    private final boolean _checks[] = new boolean[Board.SIZE + 1];
    private boolean _show_pairs;
    private boolean _show_triplets;
    private CellView _selected_cell = null;
}
