package view;


import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import model.Board;


@SuppressWarnings("serial")
public class ShowPanel extends JPanel {

    public ShowPanel(final MainWindow view) {

        _view = view;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(new JLabel("Show"));
        add(new JSeparator());

        final Action ar = new AbstractAction("rs") {

            public void actionPerformed(final ActionEvent arg) {

                reset();
            }
        };
        add(new JButton(ar));
        add(new JSeparator());

        for (int i = 1; i <= Board.SIZE; i++) {

            final Action a = new AbstractAction(((Integer) i).toString()) {

                public void actionPerformed(final ActionEvent arg) {

                    register_check((JCheckBox) arg.getSource(), arg.getActionCommand());
                }
            };
            _display[i] = new JCheckBox(a);
            add(_display[i]);
        }
        add(new JSeparator());

        final Action a = new AbstractAction("p") {

            public void actionPerformed(final ActionEvent arg) {

                _view.register_pair_highlight(_pairs.isSelected());
            }
        };
        _pairs = new JCheckBox(a);
        add(_pairs);

        final Action at = new AbstractAction("t") {

            public void actionPerformed(final ActionEvent arg) {

                _view.register_triplet_highlight(_triplets.isSelected());
            }
        };
        _triplets = new JCheckBox(at);
        add(_triplets);

        setMaximumSize(new Dimension(200, 500));
        reset();
    }


    public void reset() {

        for (int i = 1; i <= Board.SIZE; i++) {

            _display[i].setSelected(true);
            _view.register_highlight(i, true);
        }

        _pairs.setSelected(false);
        _triplets.setSelected(false);
        _view.register_pair_highlight(false);
        _view.register_triplet_highlight(false);
    }


    private void register_check(final JCheckBox src, final String value) {

        _view.register_highlight(Integer.parseInt(value), src.isSelected());
    }


    private final MainWindow _view;
    private final JCheckBox _display[] = new JCheckBox[Board.SIZE + 1];
    private final JCheckBox _pairs;
    private final JCheckBox _triplets;
}
