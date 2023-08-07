# Create Paper Line
====================

Automation path for making books from trees.

Features
---------
- Dryer simple machine with custom recipe type. `create_paper_line:drying`
- Create recipes for mixing, crushing, milling and sequenced assemply.
- JEI integration.
- Jade integration.
- KubeJS integration for the dryer.

Pending Features:
----------------
- Ponder scenes.
- Progression tweaking.
- Achievements.
- Particles.
- New textures!? (I'll try).
- Document KubeJS example
- Upload production line schematic.

### Drying recipes:
- `ingredient` is required. One item or tag.
- `result is` is required. One itemStack.
- `processingTime` is optional. Default 1.

### Drying recipe example(included):
```
{
  "type": "create_paper_line:dryer",
  "ingredient": {
      "item": "create_paper_line:wet_cardboard_sheet"
  },
  "result": {
    "item": "create_paper_line:cardboard_sheet"
  },
  "processingTime": 400
}
```