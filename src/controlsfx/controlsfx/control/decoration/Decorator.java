/**
 * Copyright (c) 2014, ControlsFX
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
package controlsfx.controlsfx.control.decoration;

import controlsfx.impl.org.controlsfx.ImplUtils;
import controlsfx.impl.org.controlsfx.skin.DecorationPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * The Decorator class is responsible for accessing decorations for a given node.
 * Through this class you may therefore add and remove decorations as desired.
 * 
 * <h3>Code Example</h3>
 * <p>Say you have a {@link javafx.scene.control.TextField} that you want to decorate. You would simply
 * do the following:
 * 
 * <pre>
 * {@code 
 * TextField textfield = new TextField();
 * Node decoration = ... // could be an ImageView or any Node!
 * Decorator.addDecoration(textfield, new GraphicDecoration(decoration, Pos.CENTER_RIGHT));}
 * </pre>
 * 
 * <p>Similarly, if we wanted to add a CSS style class (e.g. because we have some 
 * css that knows to make the 'warning' style class turn the TextField a lovely
 * shade of bright red, we would simply do the following:
 * 
 * <pre>
 * {@code 
 * TextField textfield = new TextField();
 * Decorator.addDecoration(textfield, new StyleClassDecoration("warning");}
 * </pre>
 * 
 * @see Decoration
 */
public class Decorator {
    
    
    /***************************************************************************
     *                                                                         *
     * Static fields                                                           *
     *                                                                         *
     **************************************************************************/

    private final static String DECORATIONS_PROPERTY_KEY = "$org.controlsfx.decorations$"; //$NON-NLS-1$

    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/
    
    private Decorator() {
        // no op
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Static API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Adds the given decoration to the given node.
     * @param target The node to add the decoration to.
     * @param decoration The decoration to add to the node.
     */
    public static final void addDecoration(Node target, Decoration decoration) {
        getDecorations(target, true).add(decoration);
        updateDecorationsOnNode(target, FXCollections.observableArrayList(decoration), null);
    }

    /**
     * Removes the given decoration from the given node.
     * @param target The node to remove the decoration from.
     * @param decoration The decoration to remove from the node.
     */
    public static final void removeDecoration(Node target, Decoration decoration) {
        getDecorations(target, true).remove(decoration);
        updateDecorationsOnNode(target, null, FXCollections.observableArrayList(decoration));
    }
    
    /**
     * Removes all the decorations that have previously been set on the given node.
     * @param target The node from which all previously set decorations should be removed.
     */
    public static final void removeAllDecorations(Node target) {
        List<Decoration> decorations = getDecorations(target, true);
        List<Decoration> removed = FXCollections.observableArrayList(decorations);
        
        target.getProperties().remove(DECORATIONS_PROPERTY_KEY);
        
        updateDecorationsOnNode(target, null, removed);
    }
    
    /**
     * Returns all the currently set decorations for the given node.
     * @param target The node for which all currently set decorations are required.
     * @return An ObservableList of the currently set decorations for the given node.
     */
    public static final ObservableList<Decoration> getDecorations(Node target) {
        return getDecorations(target, false);
    }

    
    
    /***************************************************************************
     *                                                                         *
     * Implementation                                                          *
     *                                                                         *
     **************************************************************************/
    
    private static final ObservableList<Decoration> getDecorations(Node target, boolean createIfAbsent) {
        @SuppressWarnings("unchecked")
        ObservableList<Decoration> decorations = (ObservableList<Decoration>) target.getProperties().get(DECORATIONS_PROPERTY_KEY);
        if (decorations == null && createIfAbsent) {
            decorations = FXCollections.observableArrayList();
            target.getProperties().put(DECORATIONS_PROPERTY_KEY, decorations);
        }
        return decorations;
    }
    
    private static void updateDecorationsOnNode(Node target, List<Decoration> added, List<Decoration> removed) {
        // find a DecorationPane parent and notify it that a node has updated
        // decorations
        getDecorationPane(target, (pane) -> pane.updateDecorationsOnNode(target, added, removed));
    }
    
    private static List<Scene> currentlyInstallingScenes = new ArrayList<>();
    
    private static void getDecorationPane(Node target, Consumer<DecorationPane> task) {
        // find a DecorationPane parent and notify it that a node has updated
        // decorations. If a DecorationPane doesn't exist, we install it into
        // the scene. If a Scene does not exist, we add a listener to try again
        // when a scene is available.
        
        DecorationPane pane = getDecorationPaneInParentHierarchy(target);
        
        if (pane != null) {
            task.accept(pane);
        } else {
            // install decoration pane
            final Consumer<Scene> sceneConsumer = scene -> {
                if (currentlyInstallingScenes.contains(scene)) {
                    return;
                }
                
                DecorationPane _pane = getDecorationPaneInParentHierarchy(target);
                if (_pane == null) {
                    currentlyInstallingScenes.add(scene);
                    _pane = new DecorationPane();
                    Node oldRoot = scene.getRoot();
                    ImplUtils.injectAsRootPane(scene, _pane, true);
                    _pane.setRoot(oldRoot);
                    currentlyInstallingScenes.remove(scene);
                }
                
                task.accept(_pane);
            };
            
            Scene scene = target.getScene();
            if (scene != null) {
                sceneConsumer.accept(scene);
            } else {
                // install listener to try again later
                InvalidationListener sceneListener = new InvalidationListener() {
                    @Override public void invalidated(Observable o) {
                        if (target.getScene() != null) {
                            sceneConsumer.accept(target.getScene());
                            target.sceneProperty().removeListener(this);
                        }
                    }
                };
                target.sceneProperty().addListener(sceneListener);
            }
        }
    }
    
    private static DecorationPane getDecorationPaneInParentHierarchy(Node target) {
        Parent p = target.getParent();
        while (p != null) {
            if (p instanceof DecorationPane) {
                return (DecorationPane) p;
            }
            p = p.getParent();
        }
        return null;
    }
}