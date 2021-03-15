package hmda.census.records

import com.typesafe.config.ConfigFactory
import hmda.model.ResourceUtils._
import hmda.model.census.Census

object CensusRecords {

  def parseCensusFile(censusFileName: String): List[Census] = {
    val lines = fileLines(s"/$censusFileName")
    lines
      .drop(1)
      .map { s =>
        val values = s.split("\\|", -1).map(_.trim).toList
        Census(
          collectionYear = values(0).toInt,
          msaMd = values(1).toInt,
          state = values(2),
          county = values(3),
          tract = values(4),
          medianIncome = values(5).toInt,
          population = values(6).toInt,
          minorityPopulationPercent = if (values(7).isEmpty) 0.0 else values(7).toDouble,
          occupiedUnits = if (values(8).isEmpty) 0 else values(8).toInt,
          oneToFourFamilyUnits = if (values(9).isEmpty) 0 else values(9).toInt,
          tractMfi = if (values(10).isEmpty) 0 else values(10).toInt,
          tracttoMsaIncomePercent = if (values(11).isEmpty) 0.0 else values(11).toDouble,
          medianAge = if (values(12).isEmpty) -1 else values(12).toInt,
          smallCounty = if (!values(13).isEmpty && values(13) == "S") true else false,
          name = values(14)
        )
      }
      .toList
  }
  
  val config = ConfigFactory.load()

  val censusFileName2018 =
      config.getString("hmda.census.fields.2018.filename")

  val censusFileName2019 =
      config.getString("hmda.census.fields.2019.filename")

  val censusFileName2020 =
    config.getString("hmda.census.fields.2020.filename")

  val (indexedTract2018: Map[String, Census], indexedCounty2018: Map[String, Census], indexedSmallCounty2018: Map[String, Census]) =
    parseCensusFile(censusFileName2018).foldLeft((Map[String, Census](), Map[String, Census](), Map[String, Census]())) {
      case ((m1, m2, m3), c) =>
        (
          m1 + (c.toHmdaTract  -> c),
          m2 + (c.toHmdaCounty -> c),
          if (c.smallCounty)
            m3 + (c.toHmdaCounty -> c)
          else m3
        )
    }

  val (indexedTract2019: Map[String, Census], indexedCounty2019: Map[String, Census], indexedSmallCounty2019: Map[String, Census]) =
    parseCensusFile(censusFileName2019).foldLeft((Map[String, Census](), Map[String, Census](), Map[String, Census]())) {
      case ((m1, m2, m3), c) =>
        (
          m1 + (c.toHmdaTract  -> c),
          m2 + (c.toHmdaCounty -> c),
          if (c.smallCounty)
            m3 + (c.toHmdaCounty -> c)
          else m3
        )
    }

  val (indexedTract2020: Map[String, Census], indexedCounty2020: Map[String, Census], indexedSmallCounty2020: Map[String, Census]) =
    parseCensusFile(censusFileName2020).foldLeft((Map[String, Census](), Map[String, Census](), Map[String, Census]())) {
      case ((m1, m2, m3), c) =>
        (
          m1 + (c.toHmdaTract  -> c),
          m2 + (c.toHmdaCounty -> c),
          if (c.smallCounty)
            m3 + (c.toHmdaCounty -> c)
          else m3
        )
    }

  def yearTractMap (year: Int) = {
    year match {
      case 2018 =>
        indexedTract2018
      case 2019 =>
        indexedTract2019
      case 2020 =>
        indexedTract2020
      case 2021 =>
        indexedTract2020
      case _ =>
        indexedTract2020
    }
  }

}
