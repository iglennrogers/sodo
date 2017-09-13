package model;


import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class Cell {

    public Cell(final int col, final int row) {

        _col = col;
        _row = row;
        _sq = ((_row / 3) * 3) + (_col / 3);
        reset();
    }


    public void reset() {

        for (int i = 1; i <= Board.SIZE; i++) {

            _possibilities.add(i);
            _colour = Color.BLUE;
        }
    }


    public boolean set_value(final int v) {

        boolean result = has_known_value();

        for (int i = 1; i <= Board.SIZE; i++) {

            if (i != v) {

                _possibilities.remove(i);
                result = true;
            }
        }
        return result;
    }


    public boolean is_unknown() {

        return (_possibilities.size() > 1);
    }


    public boolean has_known_value() {

        return (_possibilities.size() == 1);
    }


    public int value() {

        if (is_unknown()) {

            throw new IllegalArgumentException("Cell has no known value! in "
                    + toString());
        }
        return (_possibilities.toArray(new Integer[0])[0]);
    }


    public boolean remove_possibility(final int v) {

        if (has_possibility(v)) {

            _possibilities.remove(v);
            if (_possibilities.size() == 0) {

                throw new IllegalArgumentException("No possibilities left! in "
                        + toString());
            }
            return true;
        }
        return false;
    }


    public boolean add_possibility(final int v) {

        if (!has_possibility(v)) {

            _possibilities.add(v);
            return true;
        }
        return false;
    }

    public void set_colour(Color col) {

        _colour = col;
    }

    public Color get_colour() {

        return _colour;
    }

    public boolean has_possibility(final int v) {

        return _possibilities.contains(v);
    }


    public Set<Integer> get_possibilities() {

        return new HashSet<Integer>(_possibilities);
    }


    @Override
    public String toString() {

        final String s = String.format("(%d,%d) : ", _col, _row);
        if (is_unknown()) {

            return s + get_possibilities().toString();
        }
        else {

            return s + ((Integer) value()).toString();
        }
    }

    Color _colour;
    final int _col;
    final int _row;
    final int _sq;

    private final Set<Integer> _possibilities = new HashSet<Integer>();
}
