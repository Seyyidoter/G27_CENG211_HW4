package boxes;
import grid.Direction;

public abstract class Box implements Rollable {
    @Override
    public abstract void roll(Direction direction);
}
