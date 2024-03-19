import aas_core.aas3_0.enhancing.Enhancer;
import aas_core.aas3_0.jsonization.Jsonization;
import aas_core.aas3_0.reporting.Reporting;
import aas_core.aas3_0.types.enums.DataTypeDefXsd;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import aas_core.aas3_0.verification.Verification;
import aas_core.aas3_0.visitation.VisitorThrough;
import aas_core.aas3_0.xmlization.Xmlization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.xml.stream.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TestCodeForDocumentation {

    @Test
    public void testCreateAnEnvironmentWithSubmodel() {

        // We create the first element with the constructor.
        IProperty someElement = new Property(DataTypeDefXsd.INT);

        // We use setters to set the optional attributes.
        someElement.setIdShort("someElement");
        someElement.setValue("1984");

        // We create the second element.
        IBlob anotherElement = new Blob("application/octet-stream");
        anotherElement.setIdShort("anotherElement");
        anotherElement.setValue(new byte[]{0, 1, 2, 3});

        // We also directly access the element attributes.
        anotherElement.getValue().ifPresent(bytes -> bytes[3] = 4);

        // We nest the elements in a submodel.
        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someElement);
        submodelElements.add(anotherElement);
        submodel.setSubmodelElements(submodelElements);

        // We now create the environment to wrap it all up.
        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);
        IEnvironment environment = new Environment(null, submodels, null);

        // We can directly access the properties from the children
        // as well.
        ((Blob) environment
                .getSubmodels().get()
                .get(0)
                .getSubmodelElements().get()
                .get(1)).getValue().get()[3] = 3;

        // Now we can do something with the environment...
    }


    @Test
    public void testDescent() {

        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.BOOLEAN);
        someProperty.setIdShort("someProperty");

        ISubmodelElement anotherProperty = new Property(DataTypeDefXsd.BOOLEAN);
        anotherProperty.setIdShort("anotherProperty");

        ISubmodelElement yetAnotherProperty = new Property(DataTypeDefXsd.BOOLEAN);
        yetAnotherProperty.setIdShort("yetAnotherProperty");

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);
        submodelElements.add(anotherProperty);
        submodelElements.add(yetAnotherProperty);
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);
        IEnvironment environment = new Environment(null, submodels, null);

        environment.descend().forEach(iClass -> System.out.println(iClass.getClass().getSimpleName()));

        // Prints:
        // Submodel
        // Property
        // Property
        // Property
    }


    @Test
    public void testDescentFilter() {

        // We prepare the environment.
        ISubmodelElement boolProperty = new Property(DataTypeDefXsd.BOOLEAN);
        boolProperty.setIdShort("boolProperty");

        ISubmodelElement intProperty = new Property(DataTypeDefXsd.INT);
        intProperty.setIdShort("intProperty");

        ISubmodelElement anotherIntProperty = new Property(DataTypeDefXsd.INT);
        anotherIntProperty.setIdShort("anotherIntProperty");

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(boolProperty);
        submodelElements.add(intProperty);
        submodelElements.add(anotherIntProperty);
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);
        IEnvironment environment = new Environment(null, submodels, null);

        // We descend recursively over all the instances
        // and filter for the int properties.
        environment.descend().forEach(iClass -> {
            if (iClass instanceof Property) {
                Property property = (Property) iClass;
                if (property.getValueType() == DataTypeDefXsd.INT) {
                    property.getIdShort().ifPresent(System.out::println);
                }
            }
        });

        // Prints:
        // intProperty
        // anotherIntProperty
    }

    @Test
    public void testVisitation() {

        // We prepare the environment.
        ISubmodelElement boolProperty = new Property(DataTypeDefXsd.BOOLEAN);
        boolProperty.setIdShort("boolProperty");

        ISubmodelElement intProperty = new Property(DataTypeDefXsd.INT);
        intProperty.setIdShort("intProperty");

        ISubmodelElement anotherIntProperty = new Property(DataTypeDefXsd.INT);
        anotherIntProperty.setIdShort("anotherIntProperty");

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(boolProperty);
        submodelElements.add(intProperty);
        submodelElements.add(anotherIntProperty);
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);
        IEnvironment environment = new Environment(null, submodels, null);

        MyVisitor myVisitor = new MyVisitor();

        myVisitor.visit(environment);

        // Prints:
        // intProperty
        // anotherIntProperty
    }

    private class MyVisitor extends VisitorThrough {
        @Override
        public void visitProperty(IProperty that) {
            if (that.getValueType() == DataTypeDefXsd.INT) {
                that.getIdShort().ifPresent(System.out::println);
            }
        }
    }


    @Test
    public void testVerify() {

        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.BOOLEAN);

        // We intentionally introduce a mistake.
        someProperty.setIdShort("!@#$% invalid ID short (*&^_+)");

        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);

        IEnvironment environment = new Environment(null, submodels, null);

        // We verify the environment and report the errors.
        Iterable<Reporting.Error> iterable = Verification.verify(environment);
        iterable.forEach(error -> {
            System.out.println(Reporting.generateJsonPath(error.getPathSegments()));
            System.out.println(error.getCause());
        });

        // Prints
        // submodels[0].submodelElements[0].idShort
        // Invariant violated:
        // ID-short of Referables shall only feature letters, digits, underscore (``_``);
        // starting mandatory with a letter. *I.e.* ``[a-zA-Z][a-zA-Z0-9_]*``.

    }


    @Test
    public void testJsonSerialize() {
        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.INT);
        someProperty.setIdShort("someProperty");

        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);

        IEnvironment environment = new Environment(null, submodels, null);
        JsonNode json = Jsonization.Serialize.toJsonObject(environment);
        System.out.println(json.toString());

        // Prints
        // {"submodels":[{
        // "id":"some-unique-global-identifier"
        // ,"submodelElements":[{"idShort":"someProperty","valueType":"xs:int","modelType":"Property"}]
        // ,"modelType":"Submodel"}]}

    }


    @Test
    public void textJsonDeserialization() throws JsonProcessingException {

        // We specify the JSON as text.
        String text = "{\"submodels\":[{\"" +
                "id\":\"some-unique-global-identifier\"," +
                "\"submodelElements\":[{\"idShort\":\"someProperty\"" +
                ",\"valueType\":\"xs:int\",\"modelType\":\"Property\"}]," +
                "\"modelType\":\"Submodel\"}]}";

        // We parse text as JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(text);

        // We parse the JSON as environment.
        IEnvironment environment = Jsonization.Deserialize.deserializeEnvironment(json);

        System.out.println(environment.getSubmodels().get().get(0).getId());

        // Prints
        // some-unique-global-identifier
    }


    @Test
    public void testJsonSerializationError() throws JsonProcessingException {
        // We specify the text which is valid JSON,
        // but not a valid representation of a model.
        String text = "{\"submodels\": 42}";

        // We parse text as JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(text);

        // We parse the JSON as environment.
        try {
            IEnvironment environment = Jsonization.Deserialize.deserializeEnvironment(json);
        } catch (Jsonization.DeserializeException exception) {
            System.out.println(exception.getPath().get() + ": " + exception.getReason().get());
        }

        // Prints
        // submodels: Expected a JsonArray, but got NUMBER

    }


    @Test
    public void testXmlSerialization() throws XMLStreamException {
        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.INT);
        someProperty.setIdShort("someProperty");

        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);

        IEnvironment environment = new Environment(null, submodels, null);

        // We create the writer
        StringWriter stringOut = new StringWriter();
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(stringOut);

        // We serialize the environment to
        Xmlization.Serialize.to(environment, xmlStreamWriter);

        System.out.println(stringOut);

        // Prints (without the newlines):
        // <environment xmlns="https://admin-shell.io/aas/3/0">
        // <submodels>
        // <submodel>
        // <id>some-unique-global-identifier</id>
        // <submodelElements>
        // <property>
        // <idShort>someProperty</idShort>
        // <valueType>xs:int</valueType>
        // </property>
        // </submodelElements>
        // </submodel>
        // </submodels>
        // </environment>

    }


    @Test
    public void testXmlDeserialization() throws XMLStreamException {
        String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<environment xmlns=\"https://admin-shell.io/aas/3/0\">" +
                "<submodels><submodel>" +
                "<id>some-unique-global-identifier</id>" +
                "<submodelElements>" +
                "<property>" +
                "<idShort>someProperty</idShort>" +
                "<valueType>xs:int</valueType>" +
                "</property>" +
                "</submodelElements>" +
                "</submodel>" +
                "</submodels>" +
                "</environment>";

        // We create the reader
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        final XMLEventReader outputReader = xmlInputFactory.createXMLEventReader(new StringReader(text));

        // We de-serialize the environment from
        // an XML text.
        IEnvironment environment = Xmlization.Deserialize.deserializeEnvironment(outputReader);
        System.out.println(environment.getSubmodels().get().get(0).getId());

        // Prints
        // some-unique-global-identifier
    }


    @Test
    public void testXmlDeserializeError() throws XMLStreamException {
        // We define a valid XML, but invalid representation
        // of an AAS instance. Submodels should be a list,
        // not a number.
        String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<environment xmlns=\"https://admin-shell.io/aas/3/0\">" +
                "<submodels>" +
                "42" +
                "</submodels>" +
                "</environment>";

        // We create the reader
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        final XMLEventReader outputReader = xmlInputFactory.createXMLEventReader(new StringReader(text));

        // We de-serialize the environment from
        // an XML text.
        try {
            IEnvironment environment = Xmlization.Deserialize.deserializeEnvironment(outputReader);
        } catch (Xmlization.DeserializeException exception) {
            System.out.println(exception.getPath().get() + ": " + exception.getReason().get());
        }

        // Prints (without newlines):
        // environment/submodels/*[0]:
        // Expected a start element opening an instance of ISubmodel, but got an XML Characters
    }


    @Test
    public void testEnhancing() {
        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.INT);
        someProperty.setIdShort("someProperty");

        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);

        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        submodel.setSubmodelElements(submodelElements);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);

        IEnvironment environment = new Environment(null, submodels, null);

        // We enhance the environment recursively.
        Enhancer<ParentEnhancement> enhancer = produceParentEnhancement();
        environment = (IEnvironment) enhancer.wrap(environment);

        Queue<IClass> queue = new ArrayDeque<>();
        queue.add(environment);

        while (!queue.isEmpty()) {
            IClass instance = queue.remove();
            for (IClass child : instance.descendOnce()) {
                enhancer.mustUnwrap(child).parent = instance;
                queue.add(child);
            }
        }
        // Retrieve the parent of the first submodel
        boolean environmentIsParent = enhancer.mustUnwrap(environment.getSubmodels().get().get(0)).parent == environment;
        System.out.println(environmentIsParent);
    }

    // We define the enhancement, a parent-child relationship.
    private class ParentEnhancement {
        public IClass parent = null;
    }

    // We define the enhancement factory.
    private Enhancer<ParentEnhancement> produceParentEnhancement() {
        Function<IClass, Optional<ParentEnhancement>> enhancementFactory = iClass -> Optional.of(new ParentEnhancement());
        return new Enhancer<>(enhancementFactory);
    }


    @Test
    public void testSelectiveEnhancing() {

        // We prepare the environment.
        ISubmodelElement someProperty = new Property(DataTypeDefXsd.INT);
        someProperty.setIdShort("someProperty");

        AdministrativeInformation administration = new AdministrativeInformation(null, "1.0", null, null, null);

        List<ISubmodelElement> submodelElements = new ArrayList<>();
        submodelElements.add(someProperty);


        ISubmodel submodel = new Submodel("some-unique-global-identifier");
        submodel.setSubmodelElements(submodelElements);
        submodel.setAdministration(administration);

        List<ISubmodel> submodels = new ArrayList<>();
        submodels.add(submodel);

        IEnvironment environment = new Environment(null, submodels, null);

        // Prepare the enhancer
        AtomicLong lastId = new AtomicLong();
        Function<IClass, Optional<IdEnhancement>> enhancementFactory = iClass -> {
            if (iClass instanceof IReferable) {
                lastId.getAndIncrement();
                return Optional.of(new IdEnhancement(lastId.get()));
            }
            return Optional.empty();
        };

        Enhancer<IdEnhancement> enhancer = new Enhancer<>(enhancementFactory);
        // Enhance
        environment = (IEnvironment) enhancer.wrap(environment);

        // The submodel and property are enhanced.
        IdEnhancement enhancement = enhancer.mustUnwrap(environment.getSubmodels().get().get(0));
        System.out.println(enhancement.Id);

        // Prints:
        // 2

        enhancement = enhancer.mustUnwrap(environment.getSubmodels().get().get(0).getSubmodelElements().get().get(0));
        System.out.println(enhancement.Id);

        // Prints:
        // 1

        // The administrative information is not referable, and thus not enhanced.
        Optional<IdEnhancement> maybeEnhancement = enhancer.unwrap(
                environment.getSubmodels().get().get(0).getAdministration().get()
        );

        System.out.println(maybeEnhancement.isPresent());

        // Prints:
        // false
    }

    private class IdEnhancement {
        public long Id;

        public IdEnhancement(long id) {
            Id = id;
        }
    }


}
