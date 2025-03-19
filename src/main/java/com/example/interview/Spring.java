package com.example.interview;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/3/6 10:31
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key 狂学
 * key auto-wired和 Resource 的区别
 * @Autowired和@Resource都是用于自动注入的注解【也叫自动装配，别看到自动装配就慌了】
 * @Autowired：属于 Spring 框架的注解，可以 key 自动按照类型注入 bean。默认情况下，它要求目标 bean 必须存在。
 * @Resource：属于 JSR-250 规范的注解，提供了 key 类型和名称注入。Spring 会首先根据名称查找 bean，如果找不到再按类型查找。
 *
 * key 循环依赖
 * 循环依赖在 Spring 中通常由 A 类引用 B 类，B 类又引用 A 类引起。Spring 通过三级缓存机制（singletonFactories, earlySingletonObjects, singletonObjects）解决大部分循环依赖问题。
 * key Spring 只解决单例（singleton）模式下的循环依赖
 * 构造器在多例的情况下会比较特殊。多例 Bean 的生命周期是当它们每次被请求时都会创建新实例，不同于单例 Bean。
 * 多例 Bean 的循环依赖在 Spring 中本身是不被支持的，因此无论我们使用 setter 注入、@Autowired 字段注入或构造器注入，都会遇到问题，Spring 无法支持多例 Bean 的循环依赖。
 *
 * key 超重要的 Bean 的创建流程
 * 1、实例化（Instantiation）：Spring 创建 Bean 实例，但属性并未设置，也就是说，还未进行依赖注入。
 * 2、填充属性（Populate properties）：Spring 将依赖的 Bean 注入到 key 已经实例化但还未完全初始化的 Bean 中 (doge)。
 * 3、初始化（Initialization）：Spring 调用 @PostConstruct 方法、afterPropertiesSet 方法等来进行最终的初始化。
 *
 * Spring key 通过setter注入可以较好地处理单例 Bean 的循环依赖问题（存疑 为什么用 setter 就可以）。这里的关键点在于 Spring 的 Bean 实例化和依赖注入过程是分开进行的。
 * 处理循环依赖的解决方案
 * key Spring 在创建单例 Bean 时，使用了三级缓存来解决循环依赖问题：
 * 一级缓存：完全初始化好的单例 Bean 缓存。
 * 二级缓存：早期暴露的 Bean 实例，key 尚未完全初始化。
 * 三级缓存：存储提前曝光的 Bean 实例工厂，用于创建实际的 Bean 实例。key 用于存放单例工厂对象，如果后续需要，可以通过工厂方法创建 Bean 实例。
 * 当 Spring 检测到循环依赖时，它会 key 提前(太关键了)将该 Bean 的未完成实例（实际上是一个ObjectFactory）放入三级缓存中。当依赖注入进行到循环部分时，Spring 将从三级缓存中获取未完成的 Bean 实例，以完成整个注入。
 *
 * key 在 IOC 容器中的 Bean，它的属性是完全设置好了吗？还是不一定？
 * 在 Spring 的 IOC 容器中，Bean 的生命周期都由 Spring 管理。在实例化 Bean 对象后，Spring 依赖注入会填充 Bean 的属性。也就是说：
 * key 如果 Bean 完全初始化好并放入一级缓存中，其所有属性已经设置好。
 * key 如果 Bean 在初始化过程中（例如，有循环依赖），这个 Bean 会被半初始化状态下加入到三级缓存或者二级缓存，这时候属性可能还没完全设置好。
 *
 * key 注入机制
 * @Autowired 的不同用法：
 *  1、字段注入（Field Injection）：这是最常见的方式，@Autowired注解直接标记在字段上。
 *  2、key 传说中的 Setter 方法注入（Setter Injection）：@Autowired注解标记在 Setter 方法上，这也是可以使用的方式。
 *  3、构造器注入（Constructor Injection）：@Autowired注解标记在 key 构造方法上，Spring 4.3 + 可以省略@Autowired注解。
 *
 * key 解决循环依赖的过程
 * 在处理循环依赖时，Spring 首先通过字段注入和 Setter 方法实例化 Bean，然后在依赖注入过程中使用三级缓存解决依赖：
 * 1、实例化（Instantiation）：Bean A 和 Bean B 先被实例化，但还未进行依赖注入。
 * 2、将 Bean 放入三级缓存：在实例化过程中，Bean A 和 Bean B 的未完成实例分别被放入三级缓存，生成 Bean Factory。
 * 3、依赖注入：
 *      对于 Bean A，Spring 发现它依赖于 Bean B，会尝试获取 B 的实例。
 *      Spring 发现 Bean B 还未完全初始化，所以在三级缓存中找到 Bean B 的工厂方法，然后通过这个工厂方法生成 B 的实例，并将其放入二级缓存。
 *      将生成的 Bean B 实例注入到 Bean A。
 *      同理，对于 Bean B，Spring 发现它依赖于 Bean A，会尝试获取 A 的实例。
 *      Spring 发现 Bean A 还未完全初始化，所以在三级缓存中找到 Bean A 的工厂方法，然后通过这个工厂方法生成 A 的实例，并将其放入二级缓存。
 *      将生成的 Bean A 实例注入到 Bean B。
 * 完成初始化：
 *      Bean A 完成依赖注入后放入一级缓存。
 *      Bean B 完成依赖注入后放入一级缓存。
 * key 通过 setter 注入，Bean A 和 Bean B 的实例可以通过三级缓存机制被分别注入到对方中，解决了循环依赖问题。
 *
 * key 构造器注入在处理循环依赖时存在局限，因为这种方式在实例化一个 Bean 时需要其所有依赖 Bean 已经完全初始化并且可用。所以，构造器注入无法处理循环依赖的原因在于无法提前暴露一个未完全初始化的 Bean 实例。
 * Spring 在实例化 A 和 B 时会产生循环依赖，因为 A 必须在构造器中被完全初始化，而 B 同时也需要在构造器中被初始化。这会导致一个死锁，最终导致 BeanCurrentlyInCreationException 异常。因此，构造器注入无法解决单例的循环依赖。
 * 但是可以配合 @Lazy 解决构造器注入时的循环依赖，它的作用是在需要引用的 Bean 被第一次使用时才初始化，而不是在 Spring 容器启动时就初始化。这允许 Spring 在处理构造器注入循环依赖时能够延迟初始化某些 Bean，从而破除循环。
 * @Lazy  标在A上时的流程：
 * 当 Spring 容器启动时，B 会被立即创建，因为它没有被标记为 @Lazy。
 * 在创建 B 时，@Autowired 标记的 A 属性存在依赖，于是容器会查找 A。
 * key A 被标记为 @Lazy，这意味着 A 的实例不会马上创建，而是返回一个代理对象。
 * 当 B 完成初始化，并且需要使用 A 时，Spring 才真正创建 A 实例（此时能够注入 B 了），从而解决循环依赖。
 *
 * 使用 setter 注入 或 @Autowired 字段注入时，Spring 通过 “三级缓存” 机制来解决循环依赖。 Spring 在构造对象时，会将半初始化的对象放入三级缓存并继续创建其他依赖对象，此时可用未完全初始化的 Bean 提供依赖注入。
 *
 * Spring 的三级缓存：
 * 一级缓存（Singleton Objects）：这是一个Map类型的缓存，存储的是已经完全初始化好的bean，即完全准备好可以使用的bean实例。键是bean的名称，值是bean的实例。
 *      这个缓存在DefaultSingletonBeanRegistry类中的singletonObjects属性中。
 * 二级缓存（Early Singleton Objects）：这同样是一个Map类型的缓存，存储的是早期的bean引用，即已经实例化但还未完全初始化的bean。这些bean已经被实例化，但是可能还没有进行属性注入等操作。
 *      这个缓存在DefaultSingletonBeanRegistry类中的earlySingletonObjects属性中。
 * 三级缓存（Singleton Factories）：这也是一个Map类型的缓存，存储的是ObjectFactory对象，这些对象可以生成早期的bean引用。当一个bean正在创建过程中，如果它被其他bean依赖，那么这个正在创建的bean就会通过这个ObjectFactory来创建一个早期引用，从而解决循环依赖的问题。
 *      这个缓存在DefaultSingletonBeanRegistry类中的singletonFactories属性中。
 */

/**
 * SpringBoot
 * Spring Boot 启动过程主要分为以下几个阶段：
 * 1、启动引导：通过 SpringApplication.run() 方法启动应用程序。内部调用了多个步骤，包括设置环境变量、加载应用程序上下文等。
 * 2、准备环境：创建并配置 Environment，以便将应用程序属性加载到上下文中，包括命令行参数、系统属性、环境变量等。
 * 3、创建并准备上下文：创建 ApplicationContext 实例，并初始化 ApplicationContext，这是应用程序的主框架。【和 2 一块说】
 * 4、上下文刷新：刷新 ApplicationContext，加载所有的 Bean 定义并完成加载过程。这个步骤很关键，它会触发各种 Bean 的生命周期回调方法。
 * 5、调用启动器和监听器：调用 ApplicationRunner 和 CommandLineRunner，完成任何附加的初始化逻辑。
 */

/**
 * springboot 启动过程？AOP是在其中具体哪个过程实现的？
 * AOP 的实现过程
 * AOP（面向切面编程）在 Spring 中通过代理模式来实现
 * Bean 定义阶段：在 Spring 容器中，所有的 Bean 定义都会被解析并注册到 ApplicationContext 中。
 * AOP 代理创建：
 *      在 Bean 初始化之前，Spring 的 AOP 框架会检查 Bean 是否有与切面匹配的方法。
 *      如果有，那么 Spring AOP 会为这个 Bean 创建一个代理对象（JDK 动态代理或 CGLIB 代理）。
 * Bean 初始化：代理对象会替代原始的 Bean 继续进行初始化。
 * 方法拦截：在方法调用的时候，代理对象会拦截方法的执行，并按切面定义的顺序执行相应的横切逻辑（如 @Before、@After 等）。
 *
 * 所以 AOP 在 Spring 启动过程中，主要是在 ApplicationContext 刷新和 Bean 初始化的过程中实现的。
 */

/**
 * JSON 传二进制为什么要 Base64 编码？
 * JSON 本质上是一种文本格式，而二进制数据包含可能无法直接以文本状态表示的字节（比如图片也是二进制数据，肯定没法用 json 传吧）。因此，Base64 编码常用于在 JSON 中传递二进制数据，
 * Base64 是一种基于 64 个可打印字符来表示二进制数据的方法，目的是将二进制数据转为可打印字符，从而能够在 JSON（文本格式）中进行传输。
 * Base64 编码的具体步骤：
 *  将二进制数据转换为以 6 位为单位的字节。（以六位为单位分割）
 *  使用 Base64 字符表将每个 6 位单位映射为对应的字符。
 */

/**
 * bean 加载是有先后的，假如代码 A 写在 B 前面。
 * 当A完成了实例化并添加进了三级缓存后，就要开始为A进行属性注入了，在注入时发现A依赖了B，那么这个时候Spring又会去getBean(b)，
 * 希望拿到之后反射调用setter方法完成属性注入。【原来反射用在这了】
 * 但是此时三个缓存里面都是没有 b的，所以getBean(b)会走一遍跟 getBean(a) 一模一样的流程(一开始都是得先创建才能放进缓存嘛，所以此时缓存里都没有)
 * 区别在于，B放进了三级缓存之后进行属性注入，发现依赖A，于是去getBean(a)【getBean(a)有重载，先判断缓存中有没有，没有的话会去创建然后放缓存】，
 * 结果发现缓存里面是有 A 的，那B就能依赖注入成功了，然后初始化，最后放入一级缓存单例池。
 * 也就是 A 先加载，结果先给 B 加载成功了。
 *
 * 如果getBean(a)是去缓存拿的话，流程是这样的：
 * 一级没有二级也没有，拿到了三级的 A 工厂，于是调用工厂的 getObject 方法，实际上这方法是(getEarlyBeanReference)生成了一个 早期引用（也就是要放到二级缓存里的东西），
 * 把这个早期引用放到二级，删除三级的这个工厂，返回这个早期实例（这是 A 提前暴露出来的一个实例，其实不完整）
 *
 * 到这里不知道小伙伴们会不会有疑问，B中提前注入了一个没有经过初始化的A类型对象不会有问题吗？
 * 答：不会。
 * key 从上图中我们可以看到，虽然在创建B时会提前给B注入了一个还未初始化的A对象，但是在创建A的流程中一直使用的是注入到B中的A对象的引用，之后会根据这个引用对A进行初始化，所以这是没有问题的。
 * key 其实就是单例的原因
 *
 * --- key
 * 也就是说这个工厂啥都没干，直接将实例化阶段创建的 A 对象返回了！
 * 所以说在 key 不考虑AOP的情况下三级缓存有用嘛？讲道理，真的没什么用，我直接将这个对象放到二级缓存中不是一点问题都没有吗？
 * 那么三级缓存到底有什么作用呢？不要急，我们先把整个流程走完，在下文结合AOP分析循环依赖的时候你就能体会到三级缓存的作用！
 *
 * --- key
 * key 如果需要AOP代理，返回一个代理对象，不需要代理，直接返回当前传入的这个bean对象
 * key 我们对A进行了AOP代理的话，那么此时getEarlyBeanReference将返回一个代理后的对象，而不是实例化阶段创建的对象，这样就意味着B中注入的A将是一个代理对象而不是A的实例化阶段创建的对象。
 * key 但是 A 本身在注入 B 之后应该就是 A 本身，不是什么代理对象！！
 *
 * 在给B注入的时候为什么要注入一个代理对象？
 * 答：当我们对A进行了AOP代理时，我们本身就希望从容器中获取到的是代理A的对象而不是A本身，因此把A当作依赖进行注入时也要注入它的代理对象
 *
 * 初始化的时候是对A对象本身进行初始化，而容器中以及注入到B中的都是代理对象，这样不会有问题吗？
 * 答：不会，这是因为 key 不管是cglib代理还是jdk动态代理生成的代理类，内部都持有一个目标类的引用，当调用代理对象的方法时，实际会去调用目标对象的方法，A完成初始化相当于代理对象自身也完成了初始化。
 * key【代理的原理，又学到了】
 *
 * 三级缓存为什么要使用工厂而不是直接使用引用？换而言之，为什么需要这个三级缓存，直接通过二级缓存暴露一个引用不行吗？
 * key 这个工厂的目的在于延迟对实例化阶段生成的对象A的代理，只有真正发生循环依赖的时候，才去提前生成代理对象，否则只会创建一个工厂并将A的实例放入到三级缓存中，但是不会去通过这个工厂去真正创建A的动态对象
 * 好像懂了，首先这个生成A或代理A的工厂什么时候被调用，只有循环依赖时被调用，key（如果不是循环依赖，A成功初始化会放入一级缓存，后面有bean要拿A在一级就拿到了）
 * 但A不知道后面到底会不会有循环依赖，所以代码始终都会放一个工厂，工厂两种有Aop放的是代理的工厂，没aop放普通实例A的工厂，
 * key 那这样相当于延迟了代理对象的创建，只有当确实循环依赖了那不得已得提前创建代理对象并注入给B了，没有循环依赖那A就正常发育初始化完成了。
 * key 而！！后面有bean要A的代理对象，都是用初始化完的A去生成代理对象再注入了，这样才符合Spring在结合AOP跟Bean的生命周期的设计！
 * 所以如果不用这个三级缓存，理论上如果A是AOP，那么它在实例化之后就要生成代理对象放到二级里面以防后面可能有循环依赖要使用这个代理对象。
 * 我太牛了！
 * Spring结合AOP跟Bean的生命周期本身就是通过AnnotationAwareAspectJAutoProxyCreator这个后置处理器来完成的，在这个后置处理的 postProcessAfterInitialization【来自 BeanPostProcessor 接口】 方法中对初始化后的Bean完成AOP代理。
 * 如果出现了循环依赖，那没有办法，只有给Bean先创建代理，但是没有出现循环依赖的情况下，key 设计之初就是让Bean在生命周期的最后一步完成代理而不是在实例化后就立马完成代理。
 *
 * 三级缓存真的提高了效率了吗？分为两点讨论：
 * 1、没有进行AOP的Bean间的循环依赖
 * 从上文分析可以看出，这种情况下三级缓存根本没用！所以不会存在什么提高了效率的说法
 * 2、进行了AOP的Bean间的循环依赖
 * 上面两个流程的唯一区别在于为A对象创建代理的时机不同，在使用了三级缓存的情况下为A创建代理的时机是在B中需要注入A的时候，而不使用三级缓存的话在A实例化后就需要马上为A创建代理然后放入到二级缓存中去。对于整个A、B的创建过程而言，消耗的时间是一样的
 * key 综上，不管是哪种情况，三级缓存提高了效率这种说法都是错误的！
 *
 * --- key
 * 总结
 * 面试官：”Spring是如何解决的循环依赖？“
 * 答：Spring通过三级缓存解决了循环依赖，其中一级缓存为单例池（singletonObjects）,二级缓存为早期曝光对象earlySingletonObjects，三级缓存为早期曝光对象工厂（singletonFactories）。
 * 当A、B两个类发生循环引用时，在A完成实例化后，就使用实例化后的对象去创建一个对象工厂，并添加到三级缓存中，如果A被AOP代理，那么通过这个工厂获取到的就是A代理后的对象，如果A没有被AOP代理，那么这个工厂获取到的就是A实例化的对象。
 * 当A进行属性注入时，会去创建B，同时B又依赖了A，所以创建B的同时又会去调用getBean(a)来获取需要的依赖，此时的getBean(a)会从缓存中获取，第一步，先获取到三级缓存中的工厂；第二步，调用对象工工厂的getObject方法来获取到对应的对象，得到这个对象后将其注入到B中。
 * key 紧接着B会走完它的生命周期流程，包括初始化、后置处理器等。当B创建完后，会将B再注入到A中，此时A再完成它的整个生命周期。至此，循环依赖结束！
 *
 * 面试官：”为什么要使用三级缓存呢？二级缓存能解决循环依赖吗？“
 * 答：key 如果要使用二级缓存解决循环依赖，意味着所有AOP Bean在实例化后就要完成AOP代理，这样违背了Spring设计的原则，
 * Spring在设计之初就是通过 AnnotationAwareAspectJAutoProxyCreator 这个后置处理器来在 key Bean 生命周期的最后一步来完成AOP代理，而不是在实例化后就立马进行AOP代理。

 *
 *
 * 还有关键 key Spring在创建Bean时默认会根据自然排序进行创建，所以A会先于B进行创建
 * 比如这两种情况：
 * 依赖情况	            依赖注入方式	                                循环依赖是否被解决
 * AB相互依赖（循环依赖）	A中注入B的方式为setter方法，B中注入A的方式为构造器	是
 * AB相互依赖（循环依赖）	B中注入A的方式为setter方法，A中注入B的方式为构造器	否
 * 据我猜测，根本原因除了那个顺序之外，还有就是区别于 setter 的字段注入能够将 bean 实例化 和 依赖注入 分成两阶段
 * 而构造器注入的方式则是融合在一起了，所以第二种情况 A在成功注入B之前不会暴露出自己，那么加载 B 过程中也看不到 A，死锁！
 * 答：刚刚debug了，A构造器注入B的时候，在创建实例的时候就会去找B，而没有将A的早期引用放入三级缓存中，从而造成了循环依赖错误
 *
 * 我现在对bean的生命周期有了非常好的理解！
 * https://developer.aliyun.com/article/766880 深度好文
 *
 * key 与此同时，我们应该知道，Spring在创建Bean的过程中分为三步
 * 实例化，简单理解就是new了一个对象
 * 依赖注入，为实例化中new出来的对象填充属性
 * 初始化，执行aware接口中的方法，初始化方法，完成AOP代理
 *
 * Aware 接口：Spring 提供了一组Aware接口，这些接口用来使 Bean 获取到 Spring 容器的一些特定资源。例如：
 * BeanNameAware：使 Bean 对象能感知到其在容器中的名称。
 * BeanFactoryAware：使 Bean 对象能够获取到 BeanFactory 实例。
 * ApplicationContextAware：使 Bean 对象能够获取到 ApplicationContext 实例。
 * EnvironmentAware：使 Bean 对象能够获取到 Environment 实例。
 * 这些接口都有一个统一的特点，就是继承了org.springframework.beans.factory.Aware接口，并且都有一个对应的 setter 方法。
 *
 * 初始化方法
 * 初始化方法是指在 Bean 实例化和依赖注入之后执行的方法。Spring 提供了几种方式定义初始化方法：
 * @PostConstruct 注解：在@PostConstruct注解的方法将在 Bean 初始化完成（即依赖注入完成）后调用。
 * 实现 InitializingBean 接口：通过实现 afterPropertiesSet 方法来定义初始化逻辑。
 * @Bean 配置中的 init-method 属性：在 @Bean 配置中可以通过 init-method 属性指定初始化方法。
 * DisposableBean 接口，或 @Bean 配置中的 destroy-method
 *
 * AOP 代理
 * AOP 代理的创建通常在各种 Aware 接口方法调用以及 Bean 初始化之后进行。
 */
//@Component
class A {
    private B b;

//    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}

//@Component
class B {
    private A a;

//    @Autowired
    public void setA(A a) {
        this.a = a;
    }
}
