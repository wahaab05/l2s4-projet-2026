***in this version we assume here that we can generate only path to the right and the path wont have any loops nor it will turn in the direction of the origin of the path (that is left)***
### generatePath()

1. create an empty Road
1. chose a random *endCellcoords(currentCellcoords)* to end path with on the right side 
1. create road tile with value as the currentCell and distance to the end set to 0
1. put the road tile into the head and tail of the Road
1. put the road tile into the hashMap of the Road
1. until the x coords of the tile we are in don't equal the width of the board repeat: 
    1. chose a previous cell (*prevCell*) to put the tile into[^prevCell(currentCell)]
    1. create a new tile with the value as the *prevCell* and reference to next cell as currentCell and distance to the end set to 1+(distance to the end of the cell in the currentCell) 
    1. reference a new tile from the currentCell
    1. put a new tile into the Road as the head 


### prevCell(currentCell)

1. chose the random direction that 
1. until there's no way to find a cell into the direction  we've chosen and direction equals right repeat:
    1. chose another direction
1. return the cell with coords of the currentCell +  the coords of the direction

### enum Directcion:

```
import java.util.Random;
enum Direction{
UP(0,-1),
DOWN(0,1),
LEFT(-1,0),
RIGHT(1,0);
int x;
int y;
public static random = new Random();
public Direction(x, y){
    this.x = x;
    this.y = y;
    }
public Direction randnom(){
    return Direction.values()[random.nextInt(Direction.values())];
    }
public int getX(){
    return this.x
    }
public int getY(){
    return this.y
    }
}
//   _
// ><_>
```
