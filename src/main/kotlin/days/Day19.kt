package days

import java.util.*

class Day19 : Day(19) {

    data class Assets(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val openGeode: Int = 0) {

        fun isValid(): Boolean {
            return ore >= 0 && clay >= 0 && obsidian >= 0 && openGeode >= 0
        }

        operator fun plus(other: Assets) =
            Assets(ore + other.ore, clay + other.clay, obsidian + other.obsidian, openGeode + other.openGeode)

        operator fun minus(other: Assets) =
            Assets(ore - other.ore, clay - other.clay, obsidian - other.obsidian, openGeode - other.openGeode)

        operator fun times(time: Int) = Assets(ore * time, clay * time, obsidian * time, openGeode * time)

    }

    data class RobotV2(val name: Name, val cost: Assets, val produce: Assets) {
        enum class Name {
            obsidianRobot, clayRobot, geodeRobot, oreRobot
        }

        fun buy(amount: Int, asset: Assets): Assets = asset - (cost * amount)
        fun produce(robotAmount: Int, asset: Assets): Assets = asset + (produce * robotAmount)
    }

    class Blueprint(
        val index: Int,
        val oreRobot: RobotV2,
        val clayRobot: RobotV2,
        val obsidianRobot: RobotV2,
        val geodeRobot: RobotV2

    ) {
        val maxOreNeededToBuild = listOf(clayRobot.cost.ore, obsidianRobot.cost.ore, geodeRobot.cost.ore).max()
    }

    class Blueprints(input: List<String>) {
        val blueprints: List<Blueprint>

        init {
            val blueprintRegexp =
                "Blueprint (?<index>\\d+): Each ore robot costs (?<oreRobotOre>\\d+) ore. Each clay robot costs (?<clayRobotOre>\\d+) ore. Each obsidian robot costs (?<obsidianRobotOre>\\d+) ore and (?<obsidianRobotGlay>\\d+) clay. Each geode robot costs (?<geodeRobotOre>\\d+) ore and (?<geodeRobotObsidian>\\d+) obsidian.".toRegex()

            blueprints = input.map {
                blueprintRegexp.matchEntire(it)!!.groups.let {
                    Blueprint(
                        it["index"]!!.value.toInt(),
                        oreRobot = RobotV2(
                            RobotV2.Name.oreRobot,
                            Assets(ore = it["oreRobotOre"]!!.value.toInt()),
                            Assets(ore = 1)
                        ),
                        clayRobot = RobotV2(
                            RobotV2.Name.clayRobot,
                            Assets(ore = it["clayRobotOre"]!!.value.toInt()),
                            Assets(clay = 1)
                        ),
                        obsidianRobot = RobotV2(
                            RobotV2.Name.obsidianRobot,
                            Assets(
                                ore = it["obsidianRobotOre"]!!.value.toInt(),
                                clay = it["obsidianRobotGlay"]!!.value.toInt()
                            ), Assets(obsidian = 1)
                        ),
                        geodeRobot = RobotV2(
                            RobotV2.Name.geodeRobot,
                            Assets(
                                ore = it["geodeRobotOre"]!!.value.toInt(),
                                obsidian = it["geodeRobotObsidian"]!!.value.toInt()
                            ), Assets(openGeode = 1)
                        )
                    )
                }
            }
        }
    }

    data class BuyRobot(val robot: RobotV2, val minute: Int = 0)

    class StrategyV2(
        val robots: List<BuyRobot>,
        val aggBuy: RobotAggregatedBuy,
        val blueprint: Blueprint,
        val maxMinutes: Int
    ) {

        constructor(robot: BuyRobot, blueprint: Blueprint, maxMinutes: Int) : this(
            listOf(robot), RobotAggregatedBuy(mapOf(robot.robot to 1), Assets(), robot.minute), blueprint, maxMinutes
        )

        val assets = asset()
        fun shouldBuild(robot: RobotV2): Boolean {
            if (robot.name == RobotV2.Name.oreRobot) {
                if (blueprint.maxOreNeededToBuild <= aggBuy.countRobots().oreRobot) {
                    return false
                }
            } else if (robot.name == RobotV2.Name.clayRobot) {
                if (blueprint.obsidianRobot.cost.clay <= aggBuy.countRobots().clayRobot) {
                    return false
                }
            } else if (robot.name == RobotV2.Name.obsidianRobot) {
                if (blueprint.geodeRobot.cost.obsidian <= aggBuy.countRobots().obsidianRobot) {
                    return false
                }
            }
            return true
        }

        fun buy(robot: RobotV2): StrategyV2? {
            if (!shouldBuild(robot)) {
                return null
            }
            val aggregatedBuy = aggBuy
            var assets = aggregatedBuy.assets
            for (additionalMinute in 1..maxMinutes - (aggregatedBuy.lastBuyMinute)) {
                if (robot.buy(1, assets).isValid()) {
                    val buyRobot = BuyRobot(robot, aggregatedBuy.lastBuyMinute + additionalMinute)
                    return StrategyV2(robots + buyRobot, aggBuy.buy(buyRobot), blueprint, maxMinutes)
                }
                for (robotFromCount in aggregatedBuy.robotCount) {
                    assets = robotFromCount.key.produce(robotFromCount.value, assets)
                }
            }
            return null
        }

        fun asset(minutes: Int = maxMinutes, aggBuyCur: RobotAggregatedBuy = aggBuy): Assets {
            val aggregatedBuy = aggBuyCur
            var assetsAggregated = aggregatedBuy.assets
            if (aggregatedBuy.lastBuyMinute < minutes) {
                for (robot in aggregatedBuy.robotCount) {
                    assetsAggregated =
                        robot.key.produce(robot.value * (minutes - aggregatedBuy.lastBuyMinute), assetsAggregated)
                }
            }
            return assetsAggregated
        }
    }

    class RobotAggregatedBuy(
        val robotCount: Map<RobotV2, Int>,
        val assets: Assets,
        val lastBuyMinute: Int,
    ) {
        data class RobotCount(
            val oreRobot: Int,
            val clayRobot: Int,
            val obsidianRobot: Int,
            val geodeRobot: Int
        )

        fun countRobots() = robotCount.map { it.key.name to it.value }.toMap().let {
            RobotCount(
                oreRobot = it.getOrDefault(RobotV2.Name.oreRobot, 0),
                clayRobot = it.getOrDefault(RobotV2.Name.clayRobot, 0),
                obsidianRobot = it.getOrDefault(RobotV2.Name.obsidianRobot, 0),
                geodeRobot = it.getOrDefault(RobotV2.Name.geodeRobot, 0)
            )
        }

        fun buy(robotBuy: BuyRobot): RobotAggregatedBuy {
            var currentAsset = assets
            for (robot in robotCount) {
                currentAsset = robot.key.produce(robot.value * (robotBuy.minute - lastBuyMinute - 1), currentAsset)
            }
            currentAsset = robotBuy.robot.buy(1, currentAsset)
            if (!currentAsset.isValid()) {
                throw RuntimeException("Asset ${currentAsset} is not valid after ${robotBuy}")
            }
            for (robot in robotCount) {
                currentAsset = robot.key.produce(robot.value, currentAsset)
            }

            return RobotAggregatedBuy(
                robotCount + (Pair(robotBuy.robot, robotCount[robotBuy.robot]?.inc() ?: 1)),
                currentAsset,
                robotBuy.minute
            )
        }
    }

    fun bestStrategy(blueprint: Blueprint, minutes: Int): StrategyV2 {
        println("Blueprint ${blueprint.index}")
        val stack = LinkedList<StrategyV2>()
        stack.add(StrategyV2(BuyRobot(blueprint.oreRobot), blueprint, minutes))
        var result = stack.peek()
        while (stack.isNotEmpty()) {
            val poll = stack.pollLast()
            if (poll.assets.openGeode > result.assets.openGeode) {
                result = poll
            }
            listOf(
                poll.buy(blueprint.oreRobot),
                poll.buy(blueprint.geodeRobot),
                poll.buy(blueprint.obsidianRobot),
                poll.buy(blueprint.clayRobot),
            ).filterNotNull().forEach {
                stack.addLast(it)
            }
        }
        return result
    }


    override fun partOne(): Any {
        var result = 0
        val blueprints = Blueprints(inputList)
        for (blueprint in blueprints.blueprints) {
            val bestStrategy = bestStrategy(blueprint, minutes = 24)
            println("Opened Geodes ${bestStrategy.assets.openGeode} with blueprint ${blueprint.index}")
            result += bestStrategy.assets.openGeode * blueprint.index
        }
        return result
    }

    override fun partTwo(): Any {
        val blueprints = Blueprints(inputList.take(3))
        return blueprints.blueprints.map {
            val bestStrategy = bestStrategy(it, minutes = 32)
            println("Opened Geodes ${bestStrategy.assets.openGeode} with blueprint ${it.index}")
            bestStrategy
        }.map { it.assets.openGeode }.reduce { a, b -> a * b }
    }
}

