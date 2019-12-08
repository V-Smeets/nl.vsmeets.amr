/**
 * Copyright (C) 2018 Vincent Smeets
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */
/**
 * This package defines the API to access the database.
 *
 * @author vincent
 */
package nl.vsmeets.amr.backend.database;

/*
 * @formatter:off
@startuml
scale max 2000 width

interface Table {
  Integer id
}

interface Site {
  String name
  ZoneId timeZone
}
Site -[hidden]-> Table

interface P1Telegram {
  String headerInformation
  Byte versionInformation
}
P1Telegram *-- Site

interface MeasuredMedium {
  Byte mediumId
  String name
}

interface Meter {
  String equipmentIdentifier
}
Meter *-- P1Telegram
Meter *-- MeasuredMedium

interface ElectricEnergyReading {
  ZonedDateTime dateTimeStamp
  Short tariffIndicator
  Quantity<Energy> consumedEnergy
  Quantity<Energy> producedEnergy
}
ElectricEnergyReading *-- Meter

interface ElectricPowerReading {
  ZonedDateTime dateTimeStamp
  Quantity<Power> consumedPower
  Quantity<Power> producedPower
}
ElectricPowerReading *-- Meter

interface ElectricPowerFailures {
  ZonedDateTime dateTimeStamp
  Integer nrOfPowerFailures
  Integer nrOfLongPowerFailures
}
ElectricPowerFailures *-- Meter

interface ElectricPowerFailureEvent {
  Duration failureDuration
  ZonedDateTime endOfFailtureTime
}
ElectricPowerFailureEvent *-- ElectricPowerFailures

interface ElectricPhaseVoltageErrors {
  ZonedDateTime dateTimeStamp
  Byte phaseNumber
  Integer nrOfVoltageSags
  Integer nrOfVoltageSwells
}
ElectricPhaseVoltageErrors *-- Meter

interface ElectricMessage {
  ZonedDateTime dateTimeStamp
  String textMessage
}
ElectricMessage *-- Meter

interface ElectricPhasePowerReading {
  ZonedDateTime dateTimeStamp
  Byte phaseNumber
  Quantity<ElectricPotential> instantaneousVoltage
  Quantity<ElectricCurrent> instantaneousCurrent
  Quantity<Power> instantaneousConsumedPower
  Quantity<Power> instantaneousProducedPower
}
ElectricPhasePowerReading *-- Meter

interface GasVolumeReading {
  ZonedDateTime dateTimeStamp
  Quantity<Volume> consumedGas
}
GasVolumeReading *-- Meter

interface ThermalEnergyReading {
  ZonedDateTime dateTimeStamp
  Quantity<Energy> consumedEnergy
}
ThermalEnergyReading *-- Meter

interface WaterVolumeReading {
  ZonedDateTime dateTimeStamp
  Quantity<Volume> consumedWater
}
WaterVolumeReading *-- Meter

interface SlaveEnergyReading {
  ZonedDateTime dateTimeStamp
  Quantity<Energy> consumedEnergy
}
SlaveEnergyReading *-- Meter

' Layout settings
ElectricEnergyReading -[hidden]> ElectricPowerReading
ElectricEnergyReading -[hidden]-> ElectricPhasePowerReading
ElectricPowerReading -[hidden]> ElectricPowerFailures
ElectricPowerReading -[hidden]-> GasVolumeReading
ElectricPowerFailures -[hidden]> ElectricPhaseVoltageErrors
ElectricPowerFailures -[hidden]-> ThermalEnergyReading
ElectricPhaseVoltageErrors -[hidden]> ElectricMessage
ElectricPhaseVoltageErrors -[hidden]-> WaterVolumeReading
ElectricMessage -[hidden]> ElectricPhasePowerReading
ElectricMessage -[hidden]-> SlaveEnergyReading
ElectricPhasePowerReading -[hidden]> GasVolumeReading
GasVolumeReading -[hidden]> ThermalEnergyReading
ThermalEnergyReading -[hidden]> WaterVolumeReading
WaterVolumeReading -[hidden]> SlaveEnergyReading
' SlaveEnergyReading

@enduml
 * @formatter:on
 */
