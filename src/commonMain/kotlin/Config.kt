object Config {
    const val tickRate = 1000L
    const val travelTime = 2000L
    const val sectorDistance = 100
    const val maxSectors = 10
    const val sectorDivisor = sectorDistance / 10f
    const val turnSpeed = 2
    val maxPowerPerSystem = ShipSystem.entries.associateWith { 10 }
}