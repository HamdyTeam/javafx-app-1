package org.epistatic.app1

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 **/

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.assertj.core.api.Assertions.assertThat
import org.epistatic.app1.controller.Controller
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

/**
 * Use Junit TestFX to integration test the application UI, so
 * not unit tests per se.
 */
class ItemTabIntegrationTest : ApplicationTest() {

	// Node Ids defined in the FXML file. Is a CSS Id selector
	// Use these to uniquely identify JavaFX controls for testing
	companion object {
		const val LIST_DEMO_TAB = "#listDemoTab"
		const val ITEM_LIST_VIEW = "#itemListView"
		const val ENTER_ITEM_FIELD = "#enterItemField"
		const val ADD_ITEM_BUTTON = "#addItemButton"
	}

	private lateinit var controller: Controller


	override fun start(stage: Stage?) {
		// we instantiate a loader directly so we can access the controller for testing
		val loader = FXMLLoader(javaClass.getResource("/app1/app1.fxml"))
		val root = loader.load<VBox>()
		controller = loader.getController()

		stage?.scene = Scene(root)
		stage?.show()
	}


	@Test
	fun shouldAddItemToList() {
		//ensure "List Demo" tab has focus
		clickOn(LIST_DEMO_TAB)

		//create new item for list - should appear at the end
		val newItem = "Zoo"
		clickOn(ENTER_ITEM_FIELD)
		write(newItem)
		type(KeyCode.TAB)
		clickOn(ADD_ITEM_BUTTON)

		// check model updated
		val itemCount = controller.itemListView.items.size
		assertThat(itemCount).isEqualTo(4)

		// check view updated
		val zooListItem = getListViewRow<String>(ITEM_LIST_VIEW, itemCount - 1)
		assertThat(zooListItem.text).isEqualTo(newItem)
	}


	/**
	 * Helper function to get a row from a ListView
	 *
	 * Type T is the type of the ListView data model.
	 */
	private fun <T> getListViewRow(viewId: String, row: Int): ListCell<T> {
		val listView = lookup(viewId).query<ListView<T>>()
		return from(listView).lookup(".list-cell").nth(row).query()
	}
}