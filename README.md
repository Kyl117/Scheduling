## Getting Started

Run the Main.java. Schedule.xlsx and Demand.xlsx will be generated.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources. The folder contains the following java files.
  1. `Order.java`: The class to constuct object for each object
  2. `OrderTable.java`: The class to group the order objects generated according to thier features(Kocks group, Heating system, Cutting Type, Standard).
  3. `WareHouse.java`: The class to record the data of steel blank in the warehouse.
  4. `DemandCalculator.java`: A helper class to calculate the Steel demand.
  5. `StandardToGroup.java`: A helper class which contains the data of the boundary of each Kocks group. It contains only one public function that convert a standard number to its                              corresponding kocks group. 
  6. `HeatingSystem.java`: A helper class which contains only one public function that checks the feasibility of putting two orders consecutively by heating system. 
  7. `Scheduling.java`: The class to schedule the order list using genetic algorithm.
  8. `FileReader.java`: The class to input the data from an excel file.
  9. `FileWriter.java`: The class to output the schedule to an excel file.
- `lib`: the folder to maintain dependencies


