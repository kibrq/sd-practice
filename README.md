# XCV

Use WASD to move. Press `I` to open inventory. Use W/S to scroll and mouse to equip/unequip items.
One can not equip two or more items of the same type or more than 5 items total.

Press a combination of HJKL keys and then SPACE to cast a spell:

* `J` - Fireball spell: releases a fireball with auto-guidance which targets the closest enemy.
* `HJKL` - Chain lightning: confuses the nearest weak enemy(all mobs except dragons are considered weak).
* `HH` - Heal spell: heals the hero with `level * 10` hp.
* `L` - Speed boost spell: doubles the hero speed for 2 seconds.
* `ZXC` - Enter/exit WTF mode: 2x speed, no cooldowns, single-key spells.

### Run game

```bash
./gradlew run
```

### Tests

```bash
./gradlew test
```

Architecture
----

- [Design document](https://docs.google.com/document/d/1QqwoZj0K42nyamNhSfQ_2WnOqvsAzywG9hKymnePOlI/edit?usp=sharing)
- [Diagrams](https://drive.google.com/drive/folders/1Qxy9MDED6X2xQ6ToKZKjNG6f96sCDFG0?usp=sharing)

## Contributors

Kirill Brilliantov [kibrq](https://github.com/kibrq)

Maxim Sukhodolskii [maxuh14](https://github.com/maxuh14)

Ruslan Salkaev [salkaevruslan](https://github.com/salkaevruslan)
