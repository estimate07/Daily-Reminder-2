package com.example

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidManifestTest {
  @Test
  fun `launcher activity points at real MainActivity class`() {
    val manifest = File("src/main/AndroidManifest.xml")
    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifest)
    val activities = document.getElementsByTagName("activity")
    val activity = activities.item(0)
    val androidName = activity.attributes.getNamedItem("android:name").nodeValue

    assertEquals("com.example.MainActivity", androidName)
  }
}
