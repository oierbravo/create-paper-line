package com.oierbravo.create_paper_line.registrate;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerBlock;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.simibubi.create.Create.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {


    private static final CreateRegistrate REGISTRATE = CreatePaperLine.registrate()
            .creativeModeTab(() ->  ModCreativeTab.MAIN);


    public static final BlockEntry<DryerBlock> DRYER = REGISTRATE.block("dryer", DryerBlock::new)
            .initialProperties(SharedProperties::wooden)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    public static void register() {}
}
