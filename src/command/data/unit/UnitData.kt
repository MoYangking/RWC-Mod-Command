package command.data.unit

import com.corrodinggames.rts.game.units.cj
import com.corrodinggames.rts.game.units.custom.l
import com.corrodinggames.rts.gameFramework.f.a22

object UnitData {
    // 按类别存储单位
    private val unitsByCategory = mapOf(
        UnitCategory.AIR to listOf(
            UnitInfo("aaBeamGunship", "AA激光射束战机（正常）", UnitCategory.AIR),
            UnitInfo("aaBeamGunship_afterburn", "AA激光射束战机（加速）", UnitCategory.AIR),
            UnitInfo("amphibiousJet", "两栖喷气机", UnitCategory.AIR),
            UnitInfo("bomber", "轰炸机", UnitCategory.AIR),
            UnitInfo("c_interceptor", "轻型拦截机", UnitCategory.AIR),
            UnitInfo("heavyInterceptor", "重型拦截机", UnitCategory.AIR),
            UnitInfo("c_helicopter", "直升机", UnitCategory.AIR),
            UnitInfo("lightGunship", "轻型直升机", UnitCategory.AIR),
            UnitInfo("gunShip", "武装直升机", UnitCategory.AIR),
            UnitInfo("dropship", "运输机", UnitCategory.AIR),
            UnitInfo("missileAirship", "导弹飞艇", UnitCategory.AIR),
            UnitInfo("fireBee", "火蜂", UnitCategory.AIR),
            UnitInfo("experimentalDropship", "飞行堡垒", UnitCategory.AIR)
        ),
        UnitCategory.LAND to listOf(
            UnitInfo("combatEngineer", "战斗工程师", UnitCategory.LAND),
            UnitInfo("c_laserTank", "激光坦克", UnitCategory.LAND),
            UnitInfo("c_mammothTank", "猛犸坦克", UnitCategory.LAND),
            UnitInfo("mechEngineer", "机械师", UnitCategory.LAND),
            UnitInfo("missileTank", "防空导弹坦克", UnitCategory.LAND),
            UnitInfo("plasmaTank", "等离子坦克", UnitCategory.LAND),
            UnitInfo("scout", "侦查者", UnitCategory.LAND),
            UnitInfo("c_artillery", "自行火炮", UnitCategory.LAND),
            UnitInfo("heavyArtillery", "重型火炮", UnitCategory.LAND),
            UnitInfo("c_tank", "坦克", UnitCategory.LAND),
            UnitInfo("builder", "建造者", UnitCategory.LAND),
            UnitInfo("hoverTank", "悬浮坦克", UnitCategory.LAND),
            UnitInfo("hovercraft", "运输船", UnitCategory.LAND),
            UnitInfo("heavyTank", "重型坦克", UnitCategory.LAND),
            UnitInfo("heavyHoverTank", "重型气垫坦克", UnitCategory.LAND),
            UnitInfo("mechGun", "基础机甲", UnitCategory.LAND),
            UnitInfo("mechMissile", "防空机甲", UnitCategory.LAND),
            UnitInfo("mechArtillery", "火炮机甲", UnitCategory.LAND),
            UnitInfo("mechBunker", "移动炮塔（正常）", UnitCategory.LAND),
            UnitInfo("mechBunkerDeployed", "移动炮塔（部署）", UnitCategory.LAND),
            UnitInfo("mechFlame", "喷火机甲", UnitCategory.LAND),
            UnitInfo("mechFlyingLanded", "飞行机甲（步行）", UnitCategory.LAND),
            UnitInfo("mechFlyingTakeoff", "飞行机甲（飞行）", UnitCategory.LAND),
            UnitInfo("mechHeavyMissile", "重型防空机械装甲", UnitCategory.LAND),
            UnitInfo("mechLaser", "等离子机甲", UnitCategory.LAND),
            UnitInfo("mechLightning", "特斯拉机甲", UnitCategory.LAND),
            UnitInfo("mechMinigun", "机枪机甲", UnitCategory.LAND),
            UnitInfo("experimentalHoverTank", "概念型悬浮坦克", UnitCategory.LAND),
            UnitInfo("modularSpider", "模块蜘蛛", UnitCategory.LAND),
            UnitInfo("c_experimentalTank", "概念型坦克", UnitCategory.LAND),
            UnitInfo("experimentalSpider", "实验蜘蛛", UnitCategory.LAND),
            UnitInfo("experimentalGunship", "实验武装直升机（正常）", UnitCategory.LAND),
            UnitInfo("experimentalGunshipLanded", "实验武装直升机（部署）", UnitCategory.LAND)
        ),
        UnitCategory.NAVY to listOf(
            UnitInfo("missileShip", "导弹舰", UnitCategory.NAVY),
            UnitInfo("gunBoat", "机枪艇", UnitCategory.NAVY),
            UnitInfo("battleShip", "战列舰", UnitCategory.NAVY),
            UnitInfo("attackSubmarine", "潜水艇", UnitCategory.NAVY),
            UnitInfo("builderShip", "海上建造者", UnitCategory.NAVY),
            UnitInfo("lightSub", "轻型潜艇", UnitCategory.NAVY),
            UnitInfo("nautilusSubmarine", "鹦鹉螺号（水下）", UnitCategory.NAVY),
            UnitInfo("nautilusSubmarineLand", "鹦鹉螺号（陆地上）", UnitCategory.NAVY),
            UnitInfo("nautilusSubmarineSurface", "鹦鹉螺号（水上）", UnitCategory.NAVY),
            UnitInfo("heavySub", "重型潜艇", UnitCategory.NAVY),
            UnitInfo("heavyBattleship", "重型战列舰", UnitCategory.NAVY),
            UnitInfo("heavyMissileShip", "重型导弹舰", UnitCategory.NAVY),
            UnitInfo("experiementalCarrier", "航空母舰", UnitCategory.NAVY)
        ),
        UnitCategory.STRUCTURE to listOf(
            UnitInfo("c_turret_t1", "炮塔（T1机枪）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t2_gun", "炮塔（T2机枪）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t3_gun", "炮塔（T3机枪）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t2_flame", "炮塔（喷火器）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t1_artillery", "炮塔（T1火炮）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t2_artillery", "炮塔（T2火炮）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t1_lightning", "炮塔（T1闪电炮塔）", UnitCategory.STRUCTURE),
            UnitInfo("c_turret_t2_lightning", "炮塔（T2闪电炮塔）", UnitCategory.STRUCTURE),
            UnitInfo("c_antiAirTurret", "防空炮塔（T1萨姆）", UnitCategory.STRUCTURE),
            UnitInfo("c_antiAirTurretT2", "防空炮塔（T2萨姆）", UnitCategory.STRUCTURE),
            UnitInfo("c_antiAirTurretT3", "防空炮塔（T3萨姆）", UnitCategory.STRUCTURE),
            UnitInfo("antiAirTurretFlak", "防空炮塔（高射炮）", UnitCategory.STRUCTURE),
            UnitInfo("extractorT1", "资源抽取器（T1）", UnitCategory.STRUCTURE),
            UnitInfo("extractorT2", "资源抽取器（T2）", UnitCategory.STRUCTURE),
            UnitInfo("extractorT3", "资源抽取器（T3）", UnitCategory.STRUCTURE),
            UnitInfo("extractorT3_overclocked", "资源抽取器（过载模式）", UnitCategory.STRUCTURE),
            UnitInfo("extractorT3_reinforced", "资源抽取器（保护模式）", UnitCategory.STRUCTURE),
            UnitInfo("fabricatorT1", "资源制造仪（T1）", UnitCategory.STRUCTURE),
            UnitInfo("fabricatorT2", "资源制造仪（T2）", UnitCategory.STRUCTURE),
            UnitInfo("fabricatorT3", "资源制造仪（T3）", UnitCategory.STRUCTURE),
            UnitInfo("laboratory", "实验室", UnitCategory.STRUCTURE),
            UnitInfo("laserDefence", "激光防御塔", UnitCategory.STRUCTURE),
            UnitInfo("repairbay", "修复湾", UnitCategory.STRUCTURE),
            UnitInfo("commandCenter", "指挥中心", UnitCategory.STRUCTURE),
            UnitInfo("experimentalLandFactory", "实验工厂", UnitCategory.STRUCTURE),
            UnitInfo("mechFactory", "机械工厂（T1）", UnitCategory.STRUCTURE),
            UnitInfo("mechFactoryT2", "机械工厂（T2）", UnitCategory.STRUCTURE),
            UnitInfo("landFactory", "陆军工厂", UnitCategory.STRUCTURE),
            UnitInfo("seaFactory", "海军工厂", UnitCategory.STRUCTURE),
            UnitInfo("airFactory", "空军工厂", UnitCategory.STRUCTURE),
            UnitInfo("outpostT1", "战争塔楼（T1）", UnitCategory.STRUCTURE),
            UnitInfo("outpostT2", "战争塔楼（T2）", UnitCategory.STRUCTURE),
            UnitInfo("creditsCrates", "资源箱", UnitCategory.STRUCTURE),
            UnitInfo("crystal_mid", "水晶", UnitCategory.STRUCTURE),
            UnitInfo("nukeLauncherC", "核弹发射井", UnitCategory.STRUCTURE),
            UnitInfo("antiNukeLauncherC", "反核防御", UnitCategory.STRUCTURE)
        ),
        UnitCategory.BUG to listOf(
            UnitInfo("ladybug", "瓢虫", UnitCategory.BUG),
            UnitInfo("bugBee", "爆炸虫", UnitCategory.BUG),
            UnitInfo("bugSpore", "建造虫", UnitCategory.BUG),
            UnitInfo("bugFly", "飞行建造虫", UnitCategory.BUG),
            UnitInfo("bugMeleeSmall", "噬咬虫（小）", UnitCategory.BUG),
            UnitInfo("bugMelee", "噬咬虫（正常）", UnitCategory.BUG),
            UnitInfo("bugMeleeLarge", "噬咬虫（大）", UnitCategory.BUG),
            UnitInfo("bugRanged", "喷射虫（T1）", UnitCategory.BUG),
            UnitInfo("bugRangedT2", "喷射虫（T2）", UnitCategory.BUG),
            UnitInfo("bugPickup", "运输虫", UnitCategory.BUG),
            UnitInfo("bugWasp", "飞行虫", UnitCategory.BUG),
            UnitInfo("bugMeleeT31", "阿尔法杀手", UnitCategory.BUG),
            UnitInfo("bugExtractor", "虫族资源抽取器（T1）", UnitCategory.BUG),
            UnitInfo("bugExtractorT2", "虫族资源抽取器（T2）", UnitCategory.BUG),
            UnitInfo("bugGenerator", "虫族资源制造仪（T1）", UnitCategory.BUG),
            UnitInfo("bugGeneratorT2", "虫族资源制造仪（T2）", UnitCategory.BUG),
            UnitInfo("bugNest", "虫巢", UnitCategory.BUG),
            UnitInfo("bugTurret", "虫族炮塔", UnitCategory.BUG)
        ),
        UnitCategory.HIDDEN to listOf(
            UnitInfo("mammothtank", "老版猛犸", UnitCategory.HIDDEN),
            UnitInfo("lasertank", "老版激光坦克", UnitCategory.HIDDEN),
            UnitInfo("nukeLaucher", "老版核弹发射器", UnitCategory.HIDDEN),
            UnitInfo("antiNukeLaucher", "老版核弹拦截器", UnitCategory.HIDDEN),
            UnitInfo("crystalResource", "老版水晶", UnitCategory.HIDDEN),
            UnitInfo("experimentaltank", "老版实验坦克", UnitCategory.HIDDEN)
        ),
        UnitCategory.SPECIAL to listOf(
            UnitInfo("megaTank", "超级坦克", UnitCategory.SPECIAL),
            UnitInfo("tankDestroyer", "坦克杀手", UnitCategory.SPECIAL),
            UnitInfo("wall_v", "城墙", UnitCategory.SPECIAL),
            UnitInfo("spreadingFire", "火", UnitCategory.SPECIAL),
            UnitInfo("flare_10s", "信号烟", UnitCategory.SPECIAL),
            UnitInfo("fogRevealer", "迷雾探测器", UnitCategory.SPECIAL),
            UnitInfo("missing", "错误单位", UnitCategory.SPECIAL),
            UnitInfo("zoneMarker", "安全区", UnitCategory.SPECIAL),
            UnitInfo("damagingBorder", "轰炸区", UnitCategory.SPECIAL),
            UnitInfo("editorOrBuilder", "沙盒建造者", UnitCategory.SPECIAL),
            UnitInfo("tree", "树", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_emptySlot", "空模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_antinuke", "反核模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_antiair", "防空炮塔模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_antiairT2", "防空炮塔模块T2", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_antiairFlak", "高射炮模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_artillery", "火炮模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_fabricator", "资源制造模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_fabricatorT2", "资源制造模块T2", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_gunturret", "机枪模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_gunturretT2", "机枪模块T2", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_laserdefense", "激光防御模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_shieldGen", "护盾增幅模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_smallgunturret", "等离子炮模块", UnitCategory.SPECIAL),
            UnitInfo("modularSpider_smallgunturretT2", "等离子炮模块T2", UnitCategory.SPECIAL),
            UnitInfo("dummyNonUnitWithTeam", "特殊逻辑单位", UnitCategory.SPECIAL)
        )
    )

    fun getAllGameUnit(): List<UnitInfo> {
        val units = mutableListOf<UnitInfo>()
        
        cj.values().forEach { unit ->
            units.add(UnitInfo(unit.name, unit.e(), UnitCategory.SPECIAL))
        }
        l.d.forEach { unit ->
            units.add(UnitInfo((unit as l).M, unit.e(), UnitCategory.SPECIAL))
        }
        return units.toList()
    }

    // 获取所有单位
    fun getAllUnits(): List<UnitInfo> {
        return unitsByCategory.values.flatten()
    }

    // 按类别获取单位
    fun getUnitsByCategory(category: UnitCategory): List<UnitInfo> {
        return unitsByCategory[category] ?: emptyList()
    }
} 