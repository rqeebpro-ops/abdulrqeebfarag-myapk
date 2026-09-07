package com.example.data

import com.example.data.local.ClientSubscriptionDao
import com.example.data.local.ClientSubscriptionEntity
import com.example.data.local.ServiceRequestDao
import com.example.data.local.ServiceRequestEntity
import com.example.model.ApnProfile
import com.example.model.MarketingPackage
import com.example.model.PhoneProblemType
import com.example.model.PhoneServiceDetail
import com.example.model.ServicePricing
import kotlinx.coroutines.flow.Flow

class RaqeebRepository(
    private val requestDao: ServiceRequestDao,
    private val subscriptionDao: ClientSubscriptionDao
) {

    // User Requests
    val allRequests: Flow<List<ServiceRequestEntity>> = requestDao.getAllRequests()

    suspend fun insertRequest(request: ServiceRequestEntity): Long {
        return requestDao.insertRequest(request)
    }

    suspend fun deleteRequest(id: Long) {
        requestDao.deleteById(id)
    }

    suspend fun updateRequestStatus(request: ServiceRequestEntity, newStatus: String) {
        requestDao.updateRequest(request.copy(status = newStatus))
    }

    // Owner Subscriptions & Client Database
    val allSubscriptions: Flow<List<ClientSubscriptionEntity>> = subscriptionDao.getAllSubscriptions()
    val subscribersCount: Flow<Int> = subscriptionDao.getSubscribersCount()
    val revenueYER: Flow<Int> = subscriptionDao.getTotalRevenueYER()
    val revenueSAR: Flow<Int> = subscriptionDao.getTotalRevenueSAR()
    val revenueUSD: Flow<Int> = subscriptionDao.getTotalRevenueUSD()

    fun searchSubscriptions(query: String): Flow<List<ClientSubscriptionEntity>> {
        return subscriptionDao.searchSubscriptions(query)
    }

    fun getSubscriptionsByStatus(status: String): Flow<List<ClientSubscriptionEntity>> {
        return subscriptionDao.getSubscriptionsByStatus(status)
    }

    suspend fun insertSubscription(subscription: ClientSubscriptionEntity): Long {
        return subscriptionDao.insertSubscription(subscription)
    }

    suspend fun updateSubscription(subscription: ClientSubscriptionEntity) {
        subscriptionDao.updateSubscription(subscription)
    }

    suspend fun updateSubscriptionStatus(id: Long, status: String, isApproved: Boolean) {
        subscriptionDao.updateStatus(id, status, isApproved)
    }

    suspend fun updateOwnerNotes(id: Long, notes: String) {
        subscriptionDao.updateOwnerNotes(id, notes)
    }

    suspend fun deleteSubscription(id: Long) {
        subscriptionDao.deleteById(id)
    }

    suspend fun clearAllSubscriptions() {
        subscriptionDao.clearAll()
    }

    fun getPhoneServices(): List<PhoneServiceDetail> {
        return listOf(
            PhoneServiceDetail(
                id = "data_media_recovery_service",
                type = PhoneProblemType.DATA_RECOVERY,
                title = "إسترداد البيانات والملفات والصور المحذوفة",
                description = "فحص واسترداد الصور العائلية، مقاطع الفيديو، والملفات المحذوفة من الذاكرة الداخلية وبطاقات SD بأحدث التقنيات.",
                pricing = ServicePricing(yer = 4000, sar = 30, usd = 8),
                steps = listOf(
                    "التوقف الفوري عن استخدام الهاتف أو التقاط صور جديدة لتجنب الكتابة فوق البيانات المحذوفة (Overwriting).",
                    "تشغيل فاحص الذاكرة المدمج في تطبيق رقيب للبحث في الملفات المؤقتة والذاكرة المخبأة (Cache & Thumbnails).",
                    "استخراج الصور ومقاطع الفيديو القابلة للاسترداد وحفظها مباشرة في مجلد التنزيلات.",
                    "في حالات الفورمات الكامل أو تلف الميموري: تسليم الجهاز لمختبر رقيب للاسترداد العميق عبر تفريغ ذواكر eMMC/UFS."
                ),
                warningNote = "سعر استرداد البيانات والملفات 4000 YER (أو 30 SAR / 8 USD) كاشتراك منفصل مخصص لهذه الخدمة."
            ),
            PhoneServiceDetail(
                id = "documents_recovery_service",
                type = PhoneProblemType.DOCUMENTS_RECOVERY,
                title = "استرجاع المستندات والوثائق وملفات PDF المحذوفة",
                description = "استرجاع ملفات العمل المحذوفة، مستندات Word/Excel، ملفات PDF، رسائل ومحادثات وقواعد بيانات التطبيقات المفقودة.",
                pricing = ServicePricing(yer = 3000, sar = 22, usd = 6),
                steps = listOf(
                    "فحص مجلدات التطبيقات والمستندات في مسارات النظام والأقسام المخبأة.",
                    "استخراج وثائق PDF وملفات الأوفيس المحذوفة وفك تشفير الملفات التالفة جزئياً.",
                    "إعادة بناء وتصدير المستندات إلى صيغها الأصلية القابلة للفتح والقراءة.",
                    "تأمين نسخ احتياطية سحابية ومحلية لضمان عدم فقدان الملفات مجدداً."
                ),
                warningNote = "سعر استرجاع المستندات 3000 YER (أو 22 SAR / 6 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "carrier_unlock_cdma_gsm",
                type = PhoneProblemType.CARRIER_UNLOCK,
                title = "فك شفرات الهاتف والشبكات المغلقة (CDMA / GSM)",
                description = "فك شفرات شبكات الهواتف المقفلة لجميع الشركات العالمية والمحلية لتشغيل جميع شرائح الاتصال.",
                pricing = ServicePricing(yer = 3000, sar = 22, usd = 6),
                steps = listOf(
                    "تحديد نوع الشبكة المغلق عليها الهاتف (Sprint, Verizon, AT&T, T-Mobile, Docomo).",
                    "استخراج رقم الهوية التسلسلي للجهاز (IMEI) عبر الاتصال على كود *#06#.",
                    "فحص حالة قفل الشبكة SIM Lock / Network Carrier Status.",
                    "تطبيق كود فك الشفرة الرسمي أو الترويت والبرمجة عبر الحاسوب بنظام DFS أو Odin.",
                    "تفعيل خيارات المطور وتصحيح أخطاء USB للتوصيل المباشر مع مركز رقيب للبرمجة."
                ),
                quickActionCode = "*#06#",
                warningNote = "سعر فك شفرات الهاتف 3000 YER (أو 22 SAR / 6 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "account_recovery_guide",
                type = PhoneProblemType.ACCOUNT_RECOVERY,
                title = "إسترجاع الحسابات المخترقة والمفقودة",
                description = "استعادة حسابات Google، واتساب المحظور أو المسروق، فيسبوك، إنستغرام وتيك توك مع استرجاع المصادقة الثنائية.",
                pricing = ServicePricing(yer = 3000, sar = 22, usd = 6),
                steps = listOf(
                    "استرجاع حساب Google/Gmail: التوجه لبوابة الاسترداد الرسمية عبر استخدام آخر كلمة مرور معروفة ورقم الهاتف الاحتياطي.",
                    "استرجاع واتساب المسروق: طلب إعادة إرسال كود التحقق وتفعيل التحقق بخطوتين وتجميد الجلسات السابقة.",
                    "استرجاع حساب فيسبوك وإنستغرام: طلب التحقق عبر الأصدقاء الموثوقين أو إرسال الهوية الرسمية لإدارة الدعم.",
                    "إلغاء حظر أرقام الواتساب: صياغة رسالة رسمية لفريق الدعم الفني لرفع الحظر المؤقت والدائم.",
                    "خدمة التدخل المباشر من خبراء رقيب لاسترجاع الحسابات المعقدة وتأمينها تماماً."
                ),
                warningNote = "سعر استرجاع الحسابات المخترقة 3000 YER (أو 22 SAR / 6 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "failure_lock_simulator",
                type = PhoneProblemType.FAILURE_LOCK_SIMULATOR,
                title = "محاكي الفشل وتخطي أقفال وإصلاح النظام المتقدم",
                description = "نظام المحاكاة والفحص البرمجي المتطور لتشخيص وتجاوز فشل الإقلاع، وحل أعطال النظام، وتخطي الأقفال المستعصية.",
                pricing = ServicePricing(yer = 7000, sar = 52, usd = 14),
                steps = listOf(
                    "ربط الهاتف بوضع التشخيص البرمجي ومحاكاة أعطال الإقلاع Bootloop / Crash Dump.",
                    "فحص سجلات النواة والكيرنل لتحديد سبب الانهيار وفشل الإقلاع.",
                    "تطبيق حزم الإصلاح الفوري وتخطي الأقفال المستعصية دون فقدان ملفات المستخدم الأساسية.",
                    "إعادة تهيئة أقسام التمهيد (Boot/Recovery/System) واستقرار النظام بالكامل."
                ),
                quickActionCode = "*#*#4636#*#*",
                warningNote = "سعر محاكي الفشل والأقفال المتقدمة 7000 YER (أو 52 SAR / 14 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "lock_bypass_frp",
                type = PhoneProblemType.LOCK_BYPASS,
                title = "فك إقفال الشاشة وحسابات حماية جوجل (FRP Bypass)",
                description = "حلول متقدمة لتخطي قفل الشاشة (النمط، رمز PIN، البصمة) وتخطي حساب جوجل بعد الفورمات.",
                pricing = ServicePricing(yer = 2000, sar = 15, usd = 4),
                steps = listOf(
                    "تحديد موديل الهاتف وإصدار الأندرويد بدقة من شاشة البداية أو وضع الريكفري.",
                    "في حال نسيان قفل الشاشة: استخدام طرق الاسترداد عبر حساب جوجل الرسمي (Find My Device).",
                    "في حال قفل FRP: إدخال الهاتف في وضع التنزيل (Download Mode) لأجهزة سامسونج أو Fastboot لشياومي وهواوي.",
                    "استخدام أدوات البرمجة المتخصصة لحذف حماية FRP وتخطي شاشة إثبات ملكية الحساب.",
                    "إمكانية فتح أجهزة كوالكوم وميدياتك عبر Test Point في مركز الصيانة المعتمد."
                ),
                quickActionCode = "*#*#4636#*#*",
                warningNote = "سعر فك إقفال الشاشة وFRP هو 2000 YER (أو 15 SAR / 4 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "arabization_system",
                type = PhoneProblemType.ARABIZATION,
                title = "تعريب الهاتف بالكامل وإضافة اللغات",
                description = "تعريب الأجهزة الأمريكية والكورية والصينية التي لا تحتوي على لغة عربية في القوائم الرسمية.",
                pricing = ServicePricing(yer = 2000, sar = 15, usd = 4),
                steps = listOf(
                    "تفعيل خيارات المطور (Developer Options) بالضغط 7 مرات على رقم الإصدار (Build Number).",
                    "تفعيل تصحيح أخطاء USB (USB Debugging).",
                    "منح تطبيق MoreLocale2 إذن تغيير اللغة عبر كود ADB التالي بدون روت:\n`adb shell pm grant jp.co.c_lis.ccl.morelocale android.permission.CHANGE_CONFIGURATION`",
                    "اختيار اللغة العربية لتعريب كافة واجهات وتطبيقات النظام تلقائياً.",
                    "تثبيت حزم الخطوط العربية المتناسقة ولوحات المفاتيح المدعومة.",
                    "للهواتف المستعصية: تفليش روم رسمي معرب مخصص أو CSC عبر مركز رقيب."
                ),
                settingsAction = "android.settings.LOCALE_SETTINGS",
                warningNote = "سعر تعريب الهاتف 2000 YER (أو 15 SAR / 4 USD) كاشتراك منفصل."
            ),
            PhoneServiceDetail(
                id = "data_activation_3g_4g_service",
                type = PhoneProblemType.DATA_ACTIVATION_3G_4G,
                title = "تفعيل وضبط بيانات 3G / 4G LTE",
                description = "ضبط نقاط الوصول (APN) وحل مشكلة عدم ظهور علامة البيانات في هواتف يمن موبايل وسبأفون ويو والشبكات الأخرى.",
                pricing = ServicePricing(yer = 2000, sar = 15, usd = 4),
                steps = listOf(
                    "اختيار نوع الشبكة والشريحة من قائمة ملفات الـ APN الجاهزة في التطبيق.",
                    "الضغط على زر 'نسخ الإعدادات' أو 'فتح إعدادات APN' في الهاتف.",
                    "إضافة نقطة وصول جديدة وإدخال الاسم والقيمة (مثل ymobile ليمن موبايل أو internet لشركة يو).",
                    "ضبط نوع APN على: default,mms,supl واختيار البروتوكول IPv4/IPv6.",
                    "حفظ نقطة الوصول وإعادة تشغيل بيانات الهاتف لتفعيل 4G فوراً.",
                    "في حال عدم ظهور التغطية: الاتصال برمز فحص الترددات *#*#4636#*#* وتحديد نمط LTE/CDMA أو LTE/GSM."
                ),
                quickActionCode = "*#*#4636#*#*",
                settingsAction = "android.settings.APN_SETTINGS",
                warningNote = "سعر تفعيل وضبط بيانات 3G/4G هو 2000 YER (أو 15 SAR / 4 USD) كاشتراك منفصل."
            )
        )
    }

    fun getApnProfiles(): List<ApnProfile> {
        return listOf(
            ApnProfile(
                id = "ymobile_4g",
                carrierName = "يمن موبايل (Yemen Mobile 4G LTE)",
                country = "اليمن",
                networkType = "4G LTE",
                apnName = "Yemen Mobile 4G",
                apnValue = "ymobile",
                dialNumber = "#777",
                username = "ymobile",
                password = "v4g",
                instructions = "ادخل الاسم: Yemen Mobile ، الـ APN: ymobile ، اسم المستخدم: ymobile ، كلمة المرور: v4g ، نوع الـ APN: default,supl"
            ),
            ApnProfile(
                id = "ymobile_3g",
                carrierName = "يمن موبايل 3G (CDMA)",
                country = "اليمن",
                networkType = "3G CDMA",
                apnName = "Yemen Mobile 3G",
                apnValue = "#777",
                dialNumber = "#777",
                username = "ymobile",
                password = "ymobile",
                instructions = "ادخل الاسم: YM3G ، الـ APN: #777 ، اسم المستخدم: ymobile ، كلمة المرور: ymobile"
            ),
            ApnProfile(
                id = "you_4g",
                carrierName = "يو (YOU Telecom 4G / MTN)",
                country = "اليمن",
                networkType = "4G / 3G",
                apnName = "YOU Internet",
                apnValue = "internet",
                dialNumber = "*121#",
                instructions = "ادخل الاسم: YOU Internet ، الـ APN: internet ، اترك اسم المستخدم وكلمة السر فارغين."
            ),
            ApnProfile(
                id = "sabafon_4g",
                carrierName = "سبأفون (Sabafon 4G / 3G)",
                country = "اليمن",
                networkType = "4G / 3G",
                apnName = "Sabafon Net",
                apnValue = "sabafon.net",
                dialNumber = "#121*",
                instructions = "ادخل الاسم: Sabafon ، الـ APN: sabafon.net ، نوع الـ APN: default"
            ),
            ApnProfile(
                id = "stc_sa",
                carrierName = "STC السعودية (Jawalnet)",
                country = "السعودية",
                networkType = "5G / 4G",
                apnName = "STC Jawalnet",
                apnValue = "jawalnet.com.sa",
                instructions = "ادخل الاسم: Jawalnet ، الـ APN: jawalnet.com.sa"
            ),
            ApnProfile(
                id = "mobily_sa",
                carrierName = "موبايلي (Mobily)",
                country = "السعودية",
                networkType = "5G / 4G",
                apnName = "Mobily Web",
                apnValue = "web1",
                instructions = "ادخل الاسم: Mobily Web ، الـ APN: web1 مسبق الدفع أو web2 للفواتير."
            )
        )
    }

    fun getMarketingPackages(): List<MarketingPackage> {
        return listOf(
            MarketingPackage(
                id = "pkg_paid_ads",
                titleAr = "الحملات الإعلانية الممولة الشاملة",
                subtitleAr = "إعلانات مستهدفة ومحترفة على Meta (فيسبوك وإنستغرام)، تيك توك، جوجل وسناب شات",
                icon = "campaign",
                estimatedReach = "50,000 - 300,000+ عميل مستهدف",
                startingPrice = "حسب ميزانيتك الإعلانية",
                popularTag = true,
                features = listOf(
                    "تحديد دقيق للجمهور المستهدف حسب الاهتمامات والمنطقة الجغرافية",
                    "كتابة نصوص إعلانية جذابة (Copywriting) تزيد من معدل التحويل",
                    "تصميم وتجهيز بوسترات وفيديوهات ريلز إعلانية عالية الجودة",
                    "إدارة الميزانية الإعلانية بأعلى عائد استثمار (ROAS)",
                    "تقارير دورية وإحصائيات أسبوعية مفصلة عن النتائج والمبيعات"
                )
            ),
            MarketingPackage(
                id = "pkg_social_media",
                titleAr = "إدارة حسابات السوشيال ميديا",
                subtitleAr = "إدارة احترافية متكاملة لصفحات علامتك التجارية وصناعة المحتوى اليومي",
                icon = "hub",
                estimatedReach = "تفاعل شهري مستمر ونمو حقيقي",
                startingPrice = "باقات شهرية مرنة",
                popularTag = false,
                features = listOf(
                    "وضع خطة محتوى شهرية مبتكرة ومتوافقة مع علامتك التجارية",
                    "نشر 20 - 30 تصميماً ومحتوى فيديو ريلز متقن شهرياً",
                    "الرد على رسائل واستفسارات العملاء والتعليقات باحترافية",
                    "تنسيق ومطابقة الهوية البصرية للهايلايت والبروفايل",
                    "بناء مجتمع وفي متفاعل يثق بمنتجاتك وخدماتك"
                )
            ),
            MarketingPackage(
                id = "pkg_graphic_design",
                titleAr = "تصميم الهويات البصرية والجرافيك",
                subtitleAr = "تصميم شعارات، بروفايلات شركات، بوسترات وبنرات إعلانية تعكس فخامة مشروعك",
                icon = "palette",
                estimatedReach = "هوية فريدة تلتصق بأذهان العملاء",
                startingPrice = "حسب حجم المشروع",
                popularTag = false,
                features = listOf(
                    "تصميم شعار (لوجو) احترافي مع دليل استخدام الهوية البصرية",
                    "تصميم كروت عمل، فولدرات، ورق رسمي وبنرات خارجية",
                    "تصميم قوالب السوشيال ميديا الحصرية لصفحاتك",
                    "فيديوهات موشن جرافيك ترويجية وشروحات تفاعلية",
                    "تسليم الملفات المصدرية عالية الدقة والجاهزة للطباعة فوراً"
                )
            ),
            MarketingPackage(
                id = "pkg_whatsapp_marketing",
                titleAr = "التسويق عبر الواتساب والرسائل الجماعية",
                subtitleAr = "نظام إرسال رسائل ترويجية جماعية موجهة ومصنفة لآلاف العملاء في ثوانٍ",
                icon = "chat",
                estimatedReach = "معدل فتح للرسائل يفوق 95%",
                startingPrice = "باقات رسائل مرنة",
                popularTag = true,
                features = listOf(
                    "إرسال رسائل ترويجية بضغطة زر مع الصور والروابط التفاعلية",
                    "أرقام وقوائم عملاء مصنفة حسب النشاط والاهتمام والمدينة",
                    "تفعيل بوتات الرد التلقائي الذكي على مدار الساعة (WhatsApp Bot)",
                    "حملات إعادة استهداف العملاء السابقين بعروض حصرية",
                    "حماية الأرقام من الحظر عبر تقنيات فواصل الإرسال الذكية"
                )
            ),
            MarketingPackage(
                id = "pkg_seo_web",
                titleAr = "تصميم المتاجر والمواقع وتحسين الـ SEO",
                subtitleAr = "مواقع إلكترونية سريعة ومتاجر لبيع منتجاتك مع تصدر نتائج البحث في جوجل",
                icon = "language",
                estimatedReach = "ظهور في الصفحة الأولى لمحركات البحث",
                startingPrice = "عروض خاصة لأصحاب المشاريع",
                popularTag = false,
                features = listOf(
                    "تصميم متجر إلكتروني سريع ومتوافق مع جميع الهواتف الذكية",
                    "ربط بوابات الدفع الإلكتروني وخدمات الشحن والتوصيل",
                    "تهيئة وتصدر الكلمات المفتاحية في محرك بحث جوجل (SEO)",
                    "إضافة وتوثيق نشاطك التجاري على خرائط جوجل (Google Maps)",
                    "دعم فني وحماية مستمرة وسيرفرات فائقة السرعة"
                )
            ),
            MarketingPackage(
                id = "pkg_consultation",
                titleAr = "استشارات ودراسات جدوى تسويقية",
                subtitleAr = "جلسات استشارية متخصصة لتحليل مشروعك ومضاعفة مبيعاتك وأرباحك",
                icon = "analytics",
                estimatedReach = "خطة عمل تسويقية واضحة المعالم",
                startingPrice = "استشارة أولية مجانية",
                popularTag = false,
                features = listOf(
                    "تحليل نقاط القوة والضعف والفرص التنافسية في مجالك",
                    "تحديد الشريحة الذهبية من العملاء الأكثر شراءً",
                    "هيكلة العروض التسويقية وأسعار المنتجات لتحقيق أعلى ربحية",
                    "بناء قمع المبيعات (Sales Funnel) لتحويل الزوار إلى مشترين",
                    "متابعة دورية لتطبيق الاستراتيجية وتحقيق الأهداف التسويقية"
                )
            )
        )
    }
}
