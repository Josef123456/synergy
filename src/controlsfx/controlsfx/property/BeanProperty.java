/**
 * Copyright (c) 2013, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package controlsfx.controlsfx.property;

import static controlsfx.impl.org.controlsfx.i18n.Localization.asKey;
import static controlsfx.impl.org.controlsfx.i18n.Localization.localize;

import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import controlsfx.controlsfx.control.PropertySheet.Item;
import controlsfx.controlsfx.dialog.Dialogs;
import controlsfx.controlsfx.property.editor.PropertyEditor;

/**
 * A convenience class for creating a {@link controlsfx.controlsfx.control.PropertySheet.Item} for use in the
 * {@link controlsfx.controlsfx.control.PropertySheet} control based on a property belonging to a
 * JavaBean - simply provide a {@link java.beans.PropertyDescriptor} and the rest will be
 * taken care of automatically.
 * 
 * @see controlsfx.controlsfx.control.PropertySheet.Item
 * @see controlsfx.controlsfx.control.PropertySheet
 * @see java.beans.PropertyDescriptor
 */
@SuppressWarnings("deprecation")
public class BeanProperty implements Item {

    private final Object bean;
    private final PropertyDescriptor beanPropertyDescriptor;
    private final Method readMethod;
    private boolean editable = true;

    public BeanProperty(Object bean, PropertyDescriptor propertyDescriptor) {
        this.bean = bean;
        this.beanPropertyDescriptor = propertyDescriptor;
        readMethod = propertyDescriptor.getReadMethod();
    }
    
    /** {@inheritDoc} */
    @Override public String getName() {
        return beanPropertyDescriptor.getDisplayName();
    }
    
    /** {@inheritDoc} */
    @Override public String getDescription() {
        return beanPropertyDescriptor.getShortDescription();
    }
    
    /** {@inheritDoc} */
    @Override public Class<?> getType() {
        return beanPropertyDescriptor.getPropertyType();
    }

    /** {@inheritDoc} */
    @Override public Object getValue() {
        try {
            return readMethod.invoke(bean);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override public void setValue(Object value) {
        Method writeMethod = beanPropertyDescriptor.getWriteMethod();
        if ( writeMethod != null ) {
            try {
                writeMethod.invoke(bean, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (Throwable e) {
				if (e instanceof PropertyVetoException) {
					Dialogs.create().title(localize(asKey("bean.property.change.error.title"))) //$NON-NLS-1$
							.message(e.getLocalizedMessage())
							.masthead(localize(asKey("bean.property.change.error.masthead"))) //$NON-NLS-1$
							.showError();
				} else {
					throw e;
				}
            }
        }
    }

    /** {@inheritDoc} */
    @Override public String getCategory() {
        return localize(asKey(beanPropertyDescriptor.isExpert()?
        		   "bean.property.category.expert":"bean.property.category.basic")); //$NON-NLS-1$ //$NON-NLS-2$
    }
	
    /** 
     * @return The object passed in to the constructor of the BeanProperty.
     */
    public Object getBean() {
        return bean;
    }

    /** 
     * @return The {@link java.beans.PropertyDescriptor} passed in to the constructor of
     * the BeanProperty.
     */
    public PropertyDescriptor getPropertyDescriptor() {
        return beanPropertyDescriptor;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked" })
	@Override public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        
        if (beanPropertyDescriptor.getPropertyEditorClass() != null && 
                PropertyEditor.class.isAssignableFrom(beanPropertyDescriptor.getPropertyEditorClass())) {
            
            return Optional.of((Class<PropertyEditor<?>>)beanPropertyDescriptor.getPropertyEditorClass());
        }
        
        return Item.super.getPropertyEditorClass();
    }
    
    /** {@inheritDoc} */
    @Override public boolean isEditable() {
        return editable;
    }
    
    /**
     * @param editable Whether this property should be editable in the PropertySheet.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    
}
