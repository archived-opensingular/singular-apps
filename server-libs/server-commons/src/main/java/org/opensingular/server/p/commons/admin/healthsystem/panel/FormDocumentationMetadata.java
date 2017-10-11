package org.opensingular.server.p.commons.admin.healthsystem.panel;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.opensingular.form.AtrRef;
import org.opensingular.form.SType;
import org.opensingular.form.STypeAttachmentList;
import org.opensingular.form.STypeSimple;
import org.opensingular.form.converter.EnumSInstanceConverter;
import org.opensingular.form.provider.ProviderContext;
import org.opensingular.form.type.basic.AtrBasic;
import org.opensingular.form.type.basic.SPackageBasic;
import org.opensingular.form.type.core.SPackageDocumentation;
import org.opensingular.form.type.core.STypeDate;
import org.opensingular.form.type.core.STypeDateTime;
import org.opensingular.form.type.core.attachment.STypeAttachment;
import org.opensingular.form.type.core.attachment.STypeAttachmentImage;
import org.opensingular.form.view.SMultiSelectionByCheckboxView;
import org.opensingular.form.wicket.behavior.InputMaskBehavior;
import org.opensingular.lib.commons.util.Loggable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FormDocumentationMetadata implements Serializable, Loggable {

    private String nomeCampo;
    private String habilitado;
    private String tamanho;
    private String siglaTipo;
    private String obrigatorio;
    private String observacao;
    private boolean selection;
    private boolean simple;
    private boolean upload;
    private boolean hiddenCopositeField;
    private String mensagens;
    private String regras;

    public FormDocumentationMetadata() {

    }

    public FormDocumentationMetadata(SType<?> rootType, SType<?> type) {
        this.selection = initSelection(type);
        this.simple = initSimpleType(type);
        this.upload = initUpload(type);
        this.hiddenCopositeField = initHiddenForDocumentation(type);
        if (isFormInputField()) {
            this.nomeCampo = initFieldName(type);
            this.habilitado = initEnabled(type);
            this.tamanho = initSize(type);
            this.siglaTipo = initTypeAbbreviation(type);
            this.obrigatorio = initRequired(type);
            this.observacao = initGeneralInformation(type, rootType);
            this.regras = initBusinessRules(type);
            this.mensagens = initMessages(type);
        }
    }

    private String initMessages(SType<?> type) {
        if (!type.getValidators().isEmpty()) {
            return "Ver regra de validação.";
        }
        return null;
    }

    private String initBusinessRules(SType<?> type) {
        if (getAttribute(type, SPackageBasic.ATR_UPDATE_LISTENER).isPresent()) {
            return "Ver regra de negócio";
        }
        return null;
    }

    private boolean initHiddenForDocumentation(SType<?> type) {
        return getAttribute(type, SPackageDocumentation.ATR_DOC_HIDDEN).orElse(false);
    }

    @SuppressWarnings("unchecked")
    private String initGeneralInformation(SType<?> type, SType<?> rootType) {
        List<String> observacoes = new ArrayList<>();
        getAttribute(type, SPackageBasic.ATR_DEPENDS_ON_FUNCTION).ifPresent(v ->
                observacoes.add(" Depende de: " + Joiner.on(", ").join(resolveDependsOn(rootType, type)))
        );
        getAttribute(type, SPackageBasic.ATR_SUBTITLE).ifPresent(v ->
                observacoes.add(" Subtítulo: " + v)
        );
        getAttribute(type, SPackageBasic.ATR_BASIC_MASK).ifPresent(v ->
                observacoes.add(" Máscara: " + InputMaskBehavior.Masks.valueOf(v).getMask())
        );
        getAttribute(type, SPackageBasic.ATR_MINIMUM_SIZE).ifPresent(v ->
                observacoes.add(" Mínimo: " + v)
        );
        getAttribute(type, SPackageBasic.ATR_MAXIMUM_SIZE).ifPresent(v ->
                observacoes.add(" Máximo: " + v)
        );
        if (selection && type.asAtrProvider().getConverter() instanceof EnumSInstanceConverter) {
            List<String> dominio = (List<String>) type
                    .asAtrProvider()
                    .getProvider()
                    .load(new ProviderContext())
                    .stream()
                    .map(e -> type.asAtrProvider().getDisplayFunction().apply((Serializable) e))
                    .collect(Collectors.toList());
            observacoes.add("Domínio: " + Joiner.on(", ").join(dominio));
        }
        return Joiner.on("<br>").join(observacoes);
    }

    private Set<String> resolveDependsOn(SType<?> rootType, SType<?> type) {
        Set<String> values = new TreeSet<>();
        Optional<Supplier<Collection<AtrBasic.DelayedDependsOnResolver>>> dependOn = getAttribute(type, SPackageBasic.ATR_DEPENDS_ON_FUNCTION);
        if (dependOn.isPresent()) {
            for (AtrBasic.DelayedDependsOnResolver func : dependOn.get().get()) {
                if (func != null) {
                    try {
                        func.resolve(rootType, type).stream().map(this::initFieldName).collect(() -> values, Set::add, Set::addAll);
                    } catch (Exception e) {
                        getLogger().error("Could not resolve dependent types for type: " + type.getName());
                    }
                }
            }
        }
        return values;
    }

    private <V> Optional<V> getAttribute(SType<?> type, AtrRef<?, ?, V> ref) {
        if (type.hasAttributeDefinedInHierarchy(ref)) {
            return Optional.ofNullable(type.getAttributeValue(ref));
        }
        return Optional.empty();
    }


    private String initRequired(SType<?> s) {
        if (s.asAtr().getAttributeValue(SPackageBasic.ATR_REQUIRED_FUNCTION) != null) {
            return "Condicional";
        } else {
            return s.asAtr().isRequired() ? "Sim" : "Não";
        }
    }


    private String initTypeAbbreviation(SType<?> s) {
        if (selection) {
            if (s.isList()) {
                if (s.getView() instanceof SMultiSelectionByCheckboxView) {
                    return "CKB";
                } else {
                    return "CBM";
                }
            } else {
                return "CB";
            }
        } else if (s instanceof STypeDate) {
            return "DT";
        } else if (s instanceof STypeDateTime) {
            return "DH";
        } else if (s instanceof STypeAttachmentImage) {
            return "IM";
        } else if (s instanceof STypeAttachment) {
            return "AN";
        } else if (s instanceof STypeAttachmentList) {
            return "ANM";
        } else {
            return "CT";
        }
    }


    private String initFieldName(SType<?> s) {
        String label = s.asAtr().getLabel();
        if (StringUtils.isBlank(label)) {
            label = s.getNameSimple();
        }
        return label;
    }

    private String initEnabled(SType<?> s) {
        if (s.asAtr().getAttributeValue(SPackageBasic.ATR_ENABLED_FUNCTION) != null) {
            return "Condicional";
        } else {
            return s.asAtr().isEnabled() ? "Sim" : "Não";
        }
    }

    private String initSize(SType<?> type) {
        if (selection) {
            return "N/A";
        } else if (initUpload(type)) {
            SType<?> uploadType = type;
            if (type instanceof STypeAttachmentList) {
                uploadType = ((STypeAttachmentList) type).getElementsType();
            }
            Optional<Long> value = getAttribute(uploadType, SPackageBasic.ATR_MAX_FILE_SIZE);
            if (value.isPresent()) {
                return value.get() / (1024 * 1024) + " MB";
            }
        } else {
            Optional<Integer> maxlength = getAttribute(type, SPackageBasic.ATR_MAX_LENGTH);
            if (maxlength.isPresent()) {
                return String.valueOf(maxlength.get());
            }
        }
        return "";
    }


    private boolean initSelection(SType<?> type) {
        return type.asAtrProvider().getProvider() != null;
    }


    private boolean initUpload(SType<?> type) {
        return type instanceof STypeAttachment || type instanceof STypeAttachmentList;
    }

    private boolean initSimpleType(SType<?> type) {
        return type instanceof STypeSimple;
    }

    public boolean isFormInputField() {
        return !hiddenCopositeField && (selection || upload || simple);
    }

    public String getFieldName() {
        return nomeCampo;
    }

    public String isEnabled() {
        return habilitado;
    }

    public String getFieldSize() {
        return tamanho;
    }

    public String getFieldTypeAbbreviation() {
        return siglaTipo;
    }

    public String isRequired() {
        return obrigatorio;
    }

    public boolean initSelection() {
        return selection;
    }

    public boolean isSimple() {
        return simple;
    }

    public boolean initUpload() {
        return upload;
    }

    public String getGeneralInformation() {
        return observacao;
    }

    public String getBusinessRules() {
        return regras;
    }

    public String getMessages() {
        return mensagens;
    }
}
