package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Board {

    public static int SIZE = 9;
    public static int ROOTSIZE = 3;


    public Board() {

        for (int row = 0; row < SIZE; row++) {

            for (int col = 0; col < SIZE; col++) {

                _cells[to_index(row, col)] = new Cell(col, row);
            }
        }
    }


    public void reset() {

        for (final Cell c : _cells) {

            c.reset();
        }
    }


    public Cell cell(final int row, final int col) {

        return _cells[to_index(row, col)];
    }


    private Set<Cell> neighbouring_cells(final Cell cell,
            final boolean exclude_self) {

        final HashSet<Cell> cells = cells_from_col(cell._col);
        cells.addAll(cells_from_row(cell._row));
        cells.addAll(cells_from_square(cell._sq));
        if (exclude_self) {

            cells.remove(cell);
        }
        return cells;
    }


    private HashSet<Cell> cells_from_row(final int row) {

        final HashSet<Cell> result = new HashSet<Cell>();

        for (int col = 0; col < SIZE; col++) {

            result.add(_cells[to_index(row, col)]);
        }
        return result;
    }


    private HashSet<Cell> cells_from_col(final int col) {

        final HashSet<Cell> result = new HashSet<Cell>();

        for (int row = 0; row < SIZE; row++) {

            result.add(_cells[to_index(row, col)]);
        }
        return result;
    }


    private HashSet<Cell> cells_from_square(final int sq) {

        final int row = (sq / ROOTSIZE) * ROOTSIZE;
        final int col = (sq % ROOTSIZE) * ROOTSIZE;

        final HashSet<Cell> result = new HashSet<Cell>();
        for (int offset = 0; offset < SIZE; offset++) {

            final int dcol = offset % ROOTSIZE;
            final int drow = offset / ROOTSIZE;
            result.add(_cells[to_index(row + drow, col + dcol)]);
        }
        return result;
    }


    private int to_index(final int row, final int col) {

        return (row * SIZE) + col;
    }


    public AutoCommand simplify() {

        for (int col = 0; col < Board.SIZE; col++) {

            for (int row = 0; row < Board.SIZE; row++) {

                final AutoCommand cmd = simplify_by_removing_known(row, col);
                if (cmd != null) {

                    return cmd;
                }
            }
        }

        for (int col = 0; col < Board.SIZE; col++) {

            final AutoCommand cmd = check_unique_within_cells(cells_from_col(col));
            if (cmd != null) {

                return cmd;
            }
        }

        for (int row = 0; row < Board.SIZE; row++) {

            final AutoCommand cmd = check_unique_within_cells(cells_from_row(row));
            if (cmd != null) {

                return cmd;
            }
        }

        for (int sq = 0; sq < Board.SIZE; sq++) {

            final AutoCommand cmd = check_unique_within_cells(cells_from_square(sq));
            if (cmd != null) {

                return cmd;
            }
        }

        return null;
    }


    private AutoCommand simplify_by_removing_known(final int row, final int col) {

        final Cell cell = cell(row, col);
        if (cell.has_known_value()) {

            final AutoCommand cmd = remove_known_number(cell);
            if (cmd != null) {

                return cmd;
            }
        }
        return null;
    }


    private AutoCommand remove_known_number(final Cell known_cell) {

        final int value = known_cell.value();

        final Set<Cell> cells = neighbouring_cells(known_cell, true);
        for (final Cell cell : cells) {

            if (cell.has_possibility(value)) {

                return new AutoCommand(cell,
                        Command.CommandType.RemovePossibility, value);
            }
        }
        return null;
    }


    private AutoCommand check_unique_within_cells(final Set<Cell> cells) {

        final HashMap<Integer, List<Cell>> counts = new HashMap<Integer, List<Cell>>();
        for (int i = 1; i <= Board.SIZE; i++) {

            counts.put(i, new ArrayList<Cell>());
        }

        for (final Cell cell : cells) {

            if (cell.is_unknown()) {

                for (final Integer num : cell.get_possibilities()) {

                    counts.get(num).add(cell);
                }
            }
        }

        for (final Integer unq : counts.keySet()) {

            final List<Cell> list = counts.get(unq);
            if (list.size() == 1) {

                for (final Cell cell : list) {

                    return new AutoCommand(cell, Command.CommandType.SetKnown,
                            unq);
                }
                break;
            }
        }

        return null;
    }


    private final Cell _cells[] = new Cell[SIZE * SIZE];
}
