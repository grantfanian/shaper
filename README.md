# Shaper
A simple Java Swing app that recieves shapes from a JSON API / NDJSON file, computes missing properties and displays them.

## Supported shapes
- Circle (radius, circumference, area - any as input)
- Triangle (a, b, c, area, perimeter - three sides or two sides and perimeter or area)
- Rectangle (width, height, area, perimeter - both dimensions or one and area or perimeter)

Shapes are drawn to scale when their coordinates are <= 1, otherwise they're scaled to fit.

## Shape sources
The app currently supports two shape sources:
- An HTTP POST API, responding with shapes serialized in JSON
- A source for testing that reads JSON objects from `shaperTestData.ndjson` (hardcoded in `Main.java`; starts over on file end)

## JSON-serialized Shape examples
```json
{
  "a": 0.5,
  "b": 0.5,
  "c": 0.9
}
```
```json
{
  "area": 0.5775338913002781,
  "circumference": 2.69397567183379,
  "type": "CIRCLE"
}
```
```
```