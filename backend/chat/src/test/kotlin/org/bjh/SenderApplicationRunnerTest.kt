package org.bjh

import org.bjh.dto.MessageDto
import org.bjh.service.MsgService
import org.bjh.workers.WorkReceiver
import org.bjh.workers.WorkSender
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = [(WorkerDockerTest.Companion.Initializer::class)])
class WorkerDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                TestPropertyValues
                        .of("spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                                "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672))
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @Autowired
    private lateinit var sender:WorkSender
    @Autowired
    private lateinit var receiver: WorkReceiver
    @Autowired
    private lateinit var msgService: MsgService

    @Test
    fun testFanout() {

        val msgList = listOf("Tests","stest")
        val fromUser = "1"
        val toUser = "2"
        val dto = MessageDto(msgList,fromUser,toUser,null)
        sender.send(listOf(MessageDto(msgList,fromUser,toUser,null)))

        val listFetched = receiver.receive(dto)
//        Assert.assertThat(listFetched[0].fromUser,equalTo(fromUser))
//        Assert.assertThat(listFetched[0].toUser,equalTo(toUser))

    }
}