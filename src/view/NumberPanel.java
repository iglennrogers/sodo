package view;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import model.Board;


@SuppressWarnings("serial")
public class NumberPanel extends JPanel {

    public NumberPanel(final MainWindow view) {

        _view = view;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(new JLabel("Numbers"));
        add(new JSeparator());

        for (int i = 1; i <= Board.SIZE; i++) {

            final Action a = new AbstractAction(((Integer) i).toString()) {

                public void actionPerformed(final ActionEvent arg) {

                    register_check((JRadioButton) arg.getSource(), arg.getActionCommand());
                }
            };
            _display.put(i, new JRadioButton(a));
            add(_display.get(i));
        }
        reset();

        setMaximumSize(new Dimension(200, 500));
    }


    void reset() {

        _selected = 1;
        for (final Integer v : _display.keySet()) {

            final JRadioButton rb = _display.get(v);
            rb.setEnabled(true);
            rb.setSelected(_selected == v);
        }
    }


    void select(final Set<Integer> possibilities) {

        for (final JRadioButton v : _display.values()) {

            v.setEnabled(possibilities.contains(Integer.parseInt(v.getActionCommand())));
        }
        if (!possibilities.contains(_selected)) {

            if (_selected != 0) {

                _display.get(_selected).setSelected(false);
            }
            final Integer newValue = (Integer) possibilities.toArray()[0];
            _selected = newValue;
            _display.get(newValue).setSelected(true);
        }
    }


    private void register_check(final JRadioButton src, final String value) {

        if (_selected != 0) {

            _display.get(_selected).setSelected(false);
        }
        _selected = Integer.parseInt(value);
    }


    @SuppressWarnings("unused")
    private final MainWindow _view;
    private final Map<Integer, JRadioButton> _display = new HashMap<Integer, JRadioButton>();
    private Integer _selected;
}
