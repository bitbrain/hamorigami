package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.anchor.AnchorManager;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidGameplayTest {

   private GameplaySetupLoader loader;

   @Mock
   private HamorigamiContext context;

   @Before
   public void beforeEach() {
      Gdx.files = new LwjglFiles();
      Application application = mock(Application.class);
      doAnswer(new Answer<Void>() {
         @Override
         public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            String tag = (String) invocationOnMock.getArguments()[0];
            String message = (String) invocationOnMock.getArguments()[1];
            throw new RuntimeException("Unexpected error log found: tag=" + tag + ", message=" + message);
         }
      }).when(application).error(anyString(), anyString());
      Gdx.app = application;
      AnchorManager anchorManager = new AnchorManager();
      anchorManager.addAnchor("tree", new GameObject());
      anchorManager.addAnchor("floor", new GameObject());
      when(context.getAnchorManager()).thenReturn(anchorManager);
      this.loader = new GameplaySetupLoader(context);

   }

   @Test
   public void testValidGameplayFile() throws IOException {
      GameplaySetup setup = loader.load("game.play");
      assertThat(setup.getCurrentDaySetup()).isNotNull();
      assertThat(setup.getNumberOfDays()).isGreaterThan(0);
   }
}
