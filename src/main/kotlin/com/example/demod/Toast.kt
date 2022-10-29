package com.example.demod

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import kotlin.system.exitProcess


enum class ImageStyle {
    CIRCLE, RECTANGLE
}

class Config {
    var alpha = 0.9
    var openTime = 7000.0
    var imageType = ImageStyle.RECTANGLE
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var width = 300.0
    var height = 200.0
    var minWidth = 300.0
    var minHeigth = 200.0
    var pos = ""
    var x = 0.0
    var y = 0.0
    var image = ""
    var animation = ""
    var startX = width
    var finishX = 0.0
    var flag = 1
    val btn1 = Button()
    val btn2 = Button()

    val sound = AudioClip(javaClass.getResource("/res/wilhelm_scream.mp3").toExternalForm())
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()


    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun setSize(width: Double = 300.0, height: Double = 200.0): Builder {
            config.width = width
            config.height = height
            config.startX = config.x + width
            return this
        }

        fun setImage(type: String = "Rectangle", src: String = "https://img.freepik.com/premium-photo/beautiful-purple-lilac-flowers_106630-22.jpg"): Builder {
            config.imageType = if(type == "Rectangle"){
                ImageStyle.RECTANGLE
            } else {
                ImageStyle.CIRCLE
            }
            config.image = src
            return this
        }

        fun setAnim(type: String = ""): Builder {
            config.animation = type
            return this
        }

        fun setPosAnim(pos: String): Builder {
            config.pos = pos
            when(pos) {
                "rightTop" -> setPlace(1100.0, 20.0)
                "rightBottom" -> setPlace(1100.0, 620.0)
                "leftBottom" -> setPlace(20.0, 620.0)
                "leftTop" -> setPlace(20.0, 20.0)
            }
            return this
        }

        fun setPlace(x: Double, y: Double): Builder {
            config.x = x
            config.y = y
            return this
        }

        fun setBtn(num: Int = 1): Builder {
            config.flag = num
            return this
        }

        fun setBtnName(name1: String = "btn1", name2: String = "btn2"): Builder {
            config.btn1.text = name1
            config.btn2.text = name2
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()
            return toast
        }
    }


    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)
        root.minWidth = config.minWidth
        root.minHeight = config.minHeigth
        windows.minWidth = config.minWidth
        windows.minHeight = config.minHeigth
        root.maxWidth = config.width

        windows.scene = Scene(root)
        windows.scene.fill = Color.TRANSPARENT


        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)
        val hBoxBtn = HBox()
        val vBox = VBox()
        val vBoxbtn1 = VBox()
        val vBoxbtn2 = VBox()

        message.maxWidth = config.width/2
        message.setWrapText(true)
        val flowPane = FlowPane()
        flowPane.children.add(message)
        title.maxWidth = config.width/2
        appName.maxWidth = config.width/2
        appName.setAlignment(Pos.TOP_RIGHT)
        title.setAlignment(Pos.CENTER)


        if(config.flag == 1) {
            vBoxbtn1.children.add(config.btn1)
            hBoxBtn.children.add(vBoxbtn1)
        } else {
            vBoxbtn1.children.add(config.btn1)
            vBoxbtn2.children.add(config.btn2)
            hBoxBtn.children.addAll(vBoxbtn1, vBoxbtn2)
        }

        setImage()

        vBox.children.addAll(title, flowPane, appName, hBoxBtn)
        box.children.addAll(vBox)
        root.left = box

        root.style = "-fx-background-color: #B9848C;" +
                "-fx-background-radius: 15px;" +
                "-fx-font-size: 14pt;" +
                "-fx-font-family: Lucida Grande;" +
                "-fx-padding: 10px;"

        title.style =   "-fx-text-fill: #02315E;" +
                "-fx-font-size: 18pt;" +
                "-fx-font-weight: bold;"


        message.style = "-fx-text-fill: #960BA4;" +
                "-fx-background-color: #B9848C;" +
                "-fx-border-color: #B9848C;" +
                "-fx-border: 0;" +
                "-fx-font-family: Lucida Grande;"

        appName.style = "-fx-font-family: Comic Sans MS, Comic Sans, cursive;" +
                "-fx-font-style: italic;" +
                "-fx-text-fill: #8ed3e8;"

        vBox.style = "-fx-padding: 10px;"

        vBoxbtn1.style = "-fx-padding: 10px;"

        vBoxbtn2.style = "-fx-padding: 10px;"

        hBoxBtn.style = "-fx-padding: 5px;"

        config.btn1.style = "-fx-padding: 5px 10px;" +
                "-fx-background-color: #2F70AF;" +
                "-fx-background-radius: 5px;" +
                "-fx-font-family: cursive;" +
                "-fx-text-fill: #f8b1d8"

        config.btn2.style = config.btn1.style

    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(100.0, 100.0)
        }
        else {
            Circle(50.0, 50.0, 50.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)
    }

    private fun openFadeAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = 0.0
        anim.toValue = config.alpha
        anim.cycleCount = 1
        anim.play()
    }

    private fun closeFadeAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    private fun openTransAnimation() {
        val anim = TranslateTransition(Duration.millis(1500.0), root)
        anim.fromX = config.startX
        anim.toX = config.finishX
        anim.cycleCount = 1
        anim.play()
    }

    private fun closeTransAnimation() {
        val anim = TranslateTransition(Duration.millis(1500.0), root)
        anim.fromX = config.finishX
        anim.toX = config.startX
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            exitProcess(0)
        }
        anim.play()
    }


    private fun makeSound() {
        config.sound.play()
    }

    fun start() {
        windows.show()

        windows.x = config.x
        windows.y = if(config.pos == "rightBottom" || config.pos == "leftBottom") {
            config.y - windows.height + config.minHeigth - 10.0
        } else {
            config.y
        }

        makeSound()
        if(config.animation == "Fade") {
            openFadeAnimation()
        } else {
            openTransAnimation()
        }
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if(config.animation == "Fade") {
                closeFadeAnimation()
            } else {
                closeTransAnimation()
            }
        }
        Thread(thread).start()
    }

}


class SomeClass: Application() {

    override fun start(p0: Stage?) {
        val toast = Toast.Builder()
            .setTitle("T.N.T")
            .setMessage("Iron Man 2423423423423423323  2423423423423423323 2423423423423423323 2423423423423423323 2423423423423423323")
            .setAppName("AC/DC")
            .setBtnName()
            .setBtn(2)
            .setSize(400.0)
            .setPosAnim("leftBottom")
            .setImage("Fade")
            .setAnim("Fad")
            .build()
        toast.start()

    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}