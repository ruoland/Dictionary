package org.ruoland.dictionary.dictionary.dictionary.entity;

import net.minecraft.world.entity.EntityType;
import org.ruoland.dictionary.Dictionary;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.BaseSubData;
import org.ruoland.dictionary.dictionary.dictionary.developer.category.IDictionaryAdapter;
import org.ruoland.dictionary.dictionary.dictionary.manager.TagManager;

public class EntitySubData extends BaseSubData<EntityGroupContent, IDictionaryAdapter.LivingEntityAdapter> {


    EntitySubData(EnumEntityTag tag){
        super(tag);
    }

    public void addItemContentInGroup(EntityType entityType){

        String itemGroupCutID = TagManager.getTagManager().getCutID(entityType.getDescriptionId());
        EntityGroupContent groupContent = getGroupMap().get(itemGroupCutID);

        if(groupContent == null) {
            Dictionary.LOGGER.debug("그룹이 없습니다. 그룹을 추가하고, 엔티티를 추가합니다. {}", itemGroupCutID);
            groupContent = new EntityGroupContent();
            getGroupMap().put(itemGroupCutID, groupContent);
        }

        //그룹 아이템 내에서는 아이템 스택 아이디로
        groupContent.addContent(new IDictionaryAdapter.LivingEntityAdapter(
                entityType));
        Dictionary.LOGGER.info("{}, {}가 추가되었습니다.", itemGroupCutID, entityType.getDescriptionId());

    }

}
