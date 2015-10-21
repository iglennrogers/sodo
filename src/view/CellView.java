package view;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import model.Cell;
import model.Command;
import sodo.Sodo;


@SuppressWarnings("serial")
public class CellView extends JLabel {

    CellView(final Sodo controller, final MainWindow view, final Cell cell) {

        _cell = cell;
        _controller = controller;
        _view = view;
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setOpaque(true);

        final MouseAdapter mouseAction = new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    onMouseRightClick(e);
                }
                else if (e.getButton() == MouseEvent.BUTTON1) {

                    onMouseLeftClick(e);
                }
            }
        };
        addMouseListener(mouseAction);

        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setSize(50, 50);
        _selected = false;
    }


    protected void onMouseRightClick(final MouseEvent e) {

        onMouseLeftClick(e);
        createMenu().show(this, e.getX(), e.getY());
    }


    protected void onMouseLeftClick(final MouseEvent e) {

        final Set<Integer> poss = _cell.get_possibilities();
        _view.number_panel().select(poss);
        _view.set_selection(this);
    }


    @Override
    public void paintComponent(final Graphics g) {

        super.paintComponent(g);

        if (_cell.has_known_value()) {

            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 32));
            setText(Integer.toString(_cell.value()));
        }
        else {

            final String s = create_numbers();
            setFont(new Font("Arial", Font.PLAIN, 16));
            if (_view.any_highlight()) {

                setForeground(Color.CYAN);
            }
            else {

                setForeground(Color.WHITE);
            }
            if (cell_highlight(s)) {

                setText("<html>" + s + "</html>");
            }
            else if (!(_view.show_pairs() || _view.show_triplets())) {

                setText("<html>" + s + "</html>");
            }
            else {

                setText("");
            }
        }

        if (_selected) {

            setBackground(new Color(64, 64, 255));
        }
        else {

            setBackground(Color.BLUE);
        }
    }


    private JPopupMenu createMenu() {

        final JPopupMenu menu = new JPopupMenu();
        final Set<Integer> vals = _cell.get_possibilities();
        if (vals.size() > 1) {

            final JMenuItem title = new JMenuItem("Set");
            title.setEnabled(false);
            menu.add(title);

            for (final Integer v : vals) {

                final CellAction action = new CellAction(_controller, _cell, v.toString());
                action.putValue("action", Command.CommandType.SetKnown);
                final JMenuItem item = new JMenuItem(action);
                menu.add(item);
            }
            menu.add(new JSeparator());
        }

        if (vals.size() > 1) {

            final JMenuItem title = new JMenuItem("Remove");
            title.setEnabled(false);
            menu.add(title);

            for (final Integer v : vals) {

                final CellAction action = new CellAction(_controller, _cell, v.toString());
                action.putValue("action", Command.CommandType.RemovePossibility);
                final JMenuItem item = new JMenuItem(action);
                menu.add(item);
            }
            // menu.add(new JSeparator());
        }
        /*
         * if (vals.size() < Board.SIZE) {
         * 
         * final JMenuItem title = new JMenuItem("Add");
         * title.setEnabled(false); menu.add(title);
         * 
         * for (int i = 1; i < (Board.SIZE + 1); i++) {
         * 
         * if (!vals.contains(i)) {
         * 
         * final CellAction action = new CellAction(_controller, _cell,
         * ((Integer) i).toString()); action.putValue("action",
         * Command.CommandType.AddPossibility); final JMenuItem item = new
         * JMenuItem(action); menu.add(item); } } menu.add(new JSeparator()); }
         * 
         * { final CellAction action = new CellAction(_controller, _cell,
         * "Clear"); action.putValue("action", Command.CommandType.Reset); final
         * JMenuItem item = new JMenuItem(action); menu.add(item); }
         */
        return menu;
    }


    private String create_numbers() {

        String s = new String();
        for (final Integer v : _cell.get_possibilities()) {

            if (_view.has_highlight(v.intValue())) {

                s += v.toString() + " ";
            }
        }
        return s.trim();
    }


    private boolean cell_highlight(final String s) {

        if (_view.show_pairs() && (s.length() == 3)) {

            return true;
        }
        if (_view.show_triplets() && (s.length() == 5)) {

            return true;
        }
        return false;
    }


    void set_selected(final boolean sel) {

        _selected = sel;
    }


    private final Cell _cell;
    private final Sodo _controller;
    private final MainWindow _view;
    private boolean _selected;
}
