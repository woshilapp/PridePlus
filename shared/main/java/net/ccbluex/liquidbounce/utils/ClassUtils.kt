/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils

import com.google.common.reflect.ClassPath
import net.ccbluex.liquidbounce.features.value.Value
import java.io.File
import java.io.IOException
import java.lang.reflect.Modifier
import java.net.URL
import java.util.*


object ClassUtils {

    private val cachedClasses = mutableMapOf<String, Boolean>()
    private val classList: ArrayList<Any?> = ArrayList()

    /**
     * Allows you to check for existing classes with the [className]
     */
    @JvmStatic
    fun hasClass(className: String): Boolean {
        return if (cachedClasses.containsKey(className))
            cachedClasses[className]!!
        else try {
            Class.forName(className)
            cachedClasses[className] = true

            true
        } catch (e: ClassNotFoundException) {
            cachedClasses[className] = false

            false
        }
    }

    fun getValues(clazz: Class<*>, instance: Any) = clazz.declaredFields.map { valueField ->
        valueField.isAccessible = true
        valueField[instance]
    }.filterIsInstance<Value<*>>()


    /**
     * 从指定路径下获取所有类
     *
     * @return
     * @throws Throwable
     */
    @Throws(Throwable::class)
    fun getAllClassByPath(packagename: String): ArrayList<Class<*>> {
        val list = ArrayList<Class<*>>()
        val classLoader = Thread.currentThread().getContextClassLoader()
        val path = packagename.replace('.', '/')
        val fileList: ArrayList<File> = ArrayList<File>()
        val enumeration: Enumeration<URL> = classLoader.getResources(path)
        while (enumeration.hasMoreElements()) {
            val url: URL = enumeration.nextElement()
            fileList.add(File(url.getFile()))
        }
        for (i in fileList.indices) {
            list.addAll(findClass(fileList[i], packagename))
        }
        return list
    }


    /**
     * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类 如果file本身是类文件，则加入list中进行保存，并返回
     *
     * @param file
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    @Throws(ClassNotFoundException::class)
    private fun findClass(file: File, packageName: String): ArrayList<Class<*>> {
        val list = ArrayList<Class<*>>()
        if (!file.exists()) {
            return list
        }
        val files = file.listFiles()
        for (file2 in files) {
            if (file2.isDirectory()) {
                assert(
                    !file2.getName().contains(".") // 添加断言用于判断
                )
                val arrayList = findClass(file2, packageName + "." + file2.getName())
                list.addAll(arrayList)
            } else if (file2.getName().endsWith(".class")) {
                // 保存的类文件不需要后缀.class
                list.add(Class.forName(packageName + '.' + file2.getName().substring(0, file2.getName().length - 6)))
            }
        }
        return list
    }


    /**
     * 获取指定的所有类
     *
     * @return
     * @throws Throwable
     */
    @Throws(Throwable::class)
    fun getClasses(packagePath: String, clazz: Class<*>): List<Class<*>> {
        val list = ArrayList<Class<*>>()
        // 判断是否是接口
        val allClass = getAllClassByPath(packagePath)
        for (i in allClass) {
            if (i != clazz) continue
            list.add(i)
        }
        return list
    }

    fun hasForge() = hasClass("net.minecraftforge.common.MinecraftForge")

}