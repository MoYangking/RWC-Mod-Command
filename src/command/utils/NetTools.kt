package command.utils

import com.moyang.rustedwarfare.js.RWApplication
import com.moyang.rustedwarfare.js.api.GameEngine
import com.moyang.rustedwarfare.js.api.game.Player
import com.moyang.rustedwarfare.js.api.network.*
import com.moyang.rustedwarfare.js.api.network.packet.Packet
import com.moyang.rustedwarfare.js.hook.MainDexKit
import com.moyang.rustedwarfare.js.utils.HookUtils.toMethod
import command.core.BKTree
import command.core.levenshteinDistance
import com.moyang.rustedwarfare.js.api.game.unit.MovementType
import com.moyang.rustedwarfare.js.api.network.packet.PacketTypes
import com.corrodinggames.rts.gameFramework.j.ae
object NetTools {
    val units = arrayOf("extractor",
    "landFactory",
    "airFactory",
    "seaFactory",
    "commandCenter",
    "turret",
    "antiAirTurret",
    "builder",
    "tank",
    "hoverTank",
    "artillery",
    "helicopter",
    "airShip",
    "gunShip",
    "missileShip",
    "gunBoat",
    "megaTank",
    "laserTank",
    "hovercraft",
    "ladybug",
    "battleShip",
    "tankDestroyer",
    "heavyTank",
    "heavyHoverTank",
    "laserDefence",
    "dropship",
    "tree",
    "repairbay",
    "NukeLaucher",
    "AntiNukeLaucher",
    "mammothTank",
    "experimentalTank",
    "experimentalLandFactory",
    "crystalResource",
    "wall_v",
    "fabricator",
    "attackSubmarine",
    "builderShip",
    "amphibiousJet",
    "supplyDepot",
    "experimentalHoverTank",
    "turret_artillery",
    "turret_flamethrower",
    "fogRevealer",
    "spreadingFire",
    "antiAirTurretT2",
    "turretT2",
    "turretT3",
    "damagingBorder",
    "zoneMarker",
    "editorOrBuilder",
    "dummyNonUnitWithTeam"
)
    fun sendServerMessage(message: String) {
        GameEngine.getInstance().netWorkEngine!!.sendSysMessage(message)
    }

    fun sendSystemMessage(connection: Connection?, message: String) {
        val packet = Packet.chat("系统消息", message, -1)
        
        if (connection != null) {
            connection.sendPacket(packet)
        }else {
            addMessage(message)
        }
    }

    fun sendSystemMessageToPlayer(player: Player, message: String) {
        CommandUtils.showToast(player.name!! + message)
        if (player.name == GameEngine.getInstance().netWorkEngine!!.self?.name) {
            addMessage(message)
            return
        }
        player.getConnection()?.let { connection ->
            sendSystemMessage(connection, message)
        }
    }

    fun sendSystemMessageToAllPlayer(message: String) {
        Player.getPlayers().forEach {
            sendSystemMessageToPlayer(it, message)
        }
    }

    fun addMessage(message: String) {
        if(GameEngine.getInstance().netWorkEngine!!.isGaming == true) {
            ae.a("指令消息",message)
        }
        GameEngine.getInstance().netWorkEngine!!.originalObject.aE.a(-1,"指令消息",message,null)
    }

    fun sendPacketToConnection(connection: Connection,packet: Packet) {
        connection.sendPacket(packet)
    }

    fun findConnectionByPlayer(player: Player): Connection? {
        return GameEngine.getInstance().netWorkEngine!!.getAllConnection().find { it.player?.gameID == player.gameID }
    }

    fun Connection.sendPacketSafely(packet: Packet): Boolean {
        return try {
            sendPacket(packet)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun kickPlayer(player: Player, reason: String): Boolean {
        val connection = findConnectionByPlayer(player) ?: return false
        val packet = Packet.kick(reason)
        return connection.sendPacketSafely(packet)
    }

    fun getPlayerByName(name: String): Player {
        val players = Player.getPlayers().map { it.name }
        val bkTree = BKTree(::levenshteinDistance)
        players.forEach { bkTree.add(it!!) }
        
        val closestMatch = bkTree.search(name, 4).minByOrNull { levenshteinDistance(it, name) }
        return Player.getPlayers().find { it.name == closestMatch }!!
    }

    fun Player.getConnection(): Connection? {
        return GameEngine.getInstance().netWorkEngine!!.getAllConnection().find { it.player?.name == this.name }
    }

    fun broadcast(message: String, prefix: String = "系统消息") {
        Player.getPlayers().forEach { player ->
            sendSystemMessageToPlayer(player, "$prefix: $message")
        }
    }
    
    fun findOnlinePlayers(predicate: (Player) -> Boolean): List<Player> {
        return Player.getPlayers().filter(predicate)
    }
    
    fun getPlayerNames(): List<String> {
        return Player.getPlayers().mapNotNull { it.name }
    }

    fun gameSummon(unit: String, x: Float, y: Float, team: Int): Packet {
        val out = OutputNetStream()
        out.startBlock("c", false)
        out.writeByte(team) // Team

        // COMMAND BLOCK
        out.writeBoolean(true) // Command
        out.writeInt(2)
        var utype = -2
        for (i in units.indices) {
        if (units[i] == unit) {
                utype = i
                break
            }
        }
        out.writeInt(utype)
        if (utype == -2) {
         out.writeUTF(unit)
        }
        out.writeFloat(x)
        out.writeFloat(y)
        out.writeLong(-1L) // target uid
        out.writeByte(42)
        out.writeFloat(1.0f)
        out.writeFloat(1.0f)
        out.writeBoolean(false)
        out.writeBoolean(false)
        out.writeBoolean(false)
        out.writeBoolean(false)

        // 2 unknown booleans
        out.writeBoolean(false)
        out.writeBoolean(false)

        // 2 unknown ints
        out.writeInt(-1)
        out.writeInt(-1)

        out.writeBoolean(false)
        out.writeBoolean(false)

    // Unit count
        out.writeInt(0)

    // a block
        out.writeBoolean(false)

        // a block
        out.writeBoolean(false)

        out.writeLong(-1)
        out.writeUTF(unit)

        out.writeBoolean(false)
    // 通用结尾
        out.stream.writeShort(0)
    // System action
        out.writeBoolean(true)
        out.writeByte(0)
        out.writeFloat(0f)
        out.writeFloat(0f)
        out.writeInt(5) // action type

        out.writeInt(0)
        out.writeBoolean(false)
        out.endBlock()
        return out.createPacket(PacketTypes.PACKET_TICK)
}
}
